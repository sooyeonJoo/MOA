console.log('record.js loaded');

// API 설정 가져오기
let apiConfig = null;
async function getApiConfig() {
  if (!apiConfig) {
    const res = await fetch('/api/config');
    apiConfig = await res.json();
  }
  return apiConfig;
}

document.addEventListener('DOMContentLoaded', () => {
  console.log('DOM ready');

  // — 토큰 & userId 파싱 —
  const token = localStorage.getItem('accessToken');
  const userId = localStorage.getItem('userId');
  if (!token || !userId) {
    alert('로그인이 필요합니다.');
    window.location.href = '/login.html';
    return;
  }

  // — 상단 상태 변수 & API 설정 —
  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, '0');
  const monthStr = `${yyyy}-${mm}-01`;
  const monthLabel = `${mm.replace(/^0/, '')}월`;

  // — 오늘 날짜 표시 —
  const pad = n => String(n).padStart(2,'0');
  const dd = pad(today.getDate());
  const dateInput = document.getElementById('date-input');
  dateInput.value = `${yyyy}-${mm}-${dd}`;
  document.getElementById('selected-date').textContent = `${yyyy}.${mm}.${dd}`;

  // — 달력 초기화 —
  const calendarEl = document.getElementById('calendar');
  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'dayGridMonth'
    },
    locale: 'ko',
    height: 'auto',
    dayMaxEvents: true,
    eventClick: function(info) {
      showDetailModal(info.event);
    },
    eventDidMount: function(info) {
      info.el.style.cursor = 'pointer';
    },
    dateClick: function(info) {
      // 1. 항상 입력란의 날짜는 바꾸기
      const [y, m, d] = info.dateStr.split('-');
      document.getElementById('selected-date').textContent = `${y}.${m}.${d}`;
      document.getElementById('date-input').value = info.dateStr;

      // 2. 더블클릭 감지해서 모달 띄우기
      const now = Date.now();
      if (lastClickDate === info.dateStr && now - lastClickTime < 500) {
        // 더블클릭일 때만 모달 띄우기
        if (window._lastMonthlyList) {
          const dateRecords = window._lastMonthlyList.filter(r => r.date === info.dateStr);
          showDetailModal(dateRecords, info.dateStr);
        }
      }
      lastClickDate = info.dateStr;
      lastClickTime = now;
    }
  });
  calendar.render();

  // 날짜 더블클릭 변수 선언 (위로 이동)
  let lastClickDate = null, lastClickTime = 0;

  // — 월별 데이터 캐싱 (모달용) —
  window._lastMonthlyList = [];

  // === 예산 조회 및 입력 모달 기능 ===
  const budgetLabelEl = document.getElementById('budgetLabel');
  const budgetAmountEl = document.getElementById('budgetAmount');
  const openBudgetModalBtn = document.getElementById('openBudgetModalBtn');

  budgetLabelEl.childNodes[0].textContent = `${monthLabel} 예산 : `;
  budgetAmountEl.textContent = '0원';

  // 예산 불러오기
  async function fetchBudget() {
    console.log('fetchBudget called!');
    const token = localStorage.getItem('accessToken');
    const userId = localStorage.getItem('userId');
    if (!token || !userId) return;
    console.log('budgetAmountEl:', budgetAmountEl);
    try {
      const config = await getApiConfig();
      const res = await fetch(`${config.recordServiceUrl}/budget`, {
        headers: { 
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      console.log('Budget API response status:', res.status);
      if (!res.ok) throw new Error('예산 조회 실패');
      const budgets = await res.json();
      console.log('Received budgets:', budgets, Array.isArray(budgets));
      let found = false;
      budgets.forEach(b => {
        if (!b.month) return;
        let m = b.month;
        if (m.length === 7) {
          found = found || (m === `${yyyy}-${mm}`);
        } else if (m.length === 10) {
          found = found || (m.substring(0, 7) === `${yyyy}-${mm}`);
        } else {
          const [y, mmRaw] = m.split('-');
          const mmPad = mmRaw.padStart(2, '0');
          found = found || (`${y}-${mmPad}` === `${yyyy}-${mm}`);
        }
        console.log('Budget:', b, 'b.month:', b.month, 'compare:', found);
      });
      const currentBudget = budgets.find(b => {
        if (!b.month) return false;
        let m = b.month;
        if (m.length === 7) return m === `${yyyy}-${mm}`;
        if (m.length === 10) return m.substring(0, 7) === `${yyyy}-${mm}`;
        const [y, mmRaw] = m.split('-');
        const mmPad = mmRaw.padStart(2, '0');
        return `${y}-${mmPad}` === `${yyyy}-${mm}`;
      });
      console.log('Found current budget:', currentBudget);
      const amount = currentBudget ? currentBudget.amount : 0;
      budgetAmountEl.textContent = amount.toLocaleString() + '원';
    } catch (e) {
      console.error('예산 조회 에러:', e);
      budgetAmountEl.textContent = '0원';
    }
  }
  fetchBudget();

  // 예산 입력 모달
  function showBudgetModal() {
    let oldModal = document.getElementById('budget-modal');
    if (oldModal) oldModal.remove();
    const modal = document.createElement('div');
    modal.id = 'budget-modal';
    modal.style.position = 'fixed';
    modal.style.left = '0';
    modal.style.top = '0';
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.background = 'rgba(0,0,0,0.3)';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';
    modal.style.zIndex = '9999';
    const content = document.createElement('div');
    content.style.background = '#fff';
    content.style.borderRadius = '20px';
    content.style.padding = '32px 24px';
    content.style.width = '320px';
    content.style.maxWidth = '90vw';
    content.style.boxShadow = '0 4px 24px rgba(0,0,0,0.15)';
    content.innerHTML = `
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <span style="font-size:1.1em;font-weight:bold;">${monthLabel} 예산 입력</span>
        <span style="font-size:2em;cursor:pointer;" id="close-budget-modal">&times;</span>
      </div>
      <hr style="margin:12px 0;" />
      <input id="modalBudgetInput" type="number" min="0" placeholder="금액을 입력하세요" style="width:100%;padding:10px;font-size:1.1em;border-radius:6px;border:1px solid #ccc;" />
      <button id="modalBudgetSaveBtn" class="moa-btn" style="width:100%;margin-top:18px;">저장</button>
    `;
    modal.appendChild(content);
    document.body.appendChild(modal);
    content.querySelector('#close-budget-modal').onclick = () => modal.remove();
    content.querySelector('#modalBudgetSaveBtn').onclick = async () => {
      const v = content.querySelector('#modalBudgetInput').value.trim();
      const token = localStorage.getItem('accessToken');
      const userId = localStorage.getItem('userId');
      if (!v || isNaN(Number(v))) return alert('숫자를 입력하세요.');
      try {
        const config = await getApiConfig();
        const res = await fetch(`${config.recordServiceUrl}/budget`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            userId: userId,
            amount: Number(v),
            month: `${yyyy}-${mm}`
          })
        });
        if (!res.ok) throw new Error('예산 저장 실패');
        alert('예산 저장 완료!');
        modal.remove();
        fetchBudget();
      } catch (e) {
        alert('예산 저장 중 오류: ' + e.message);
      }
    };
  }
  if (openBudgetModalBtn) {
    openBudgetModalBtn.addEventListener('click', showBudgetModal);
  } else {
    console.error('openBudgetModalBtn not found!');
  }

  let income  = parseInt(localStorage.getItem('income')  || '2500000', 10);
  let expense = parseInt(localStorage.getItem('expense') || '1500000', 10);
  const incomeEl    = document.getElementById('income');
  const expenseEl   = document.getElementById('expense');
  const balanceEl   = document.getElementById('balance');

  console.log('Config:', { userId, token });

  // — 예산 초기화 & 헤더 갱신 함수 —
  function updateHeader() {
    incomeEl.textContent  = income.toLocaleString();
    expenseEl.textContent = expense.toLocaleString();
    balanceEl.textContent = (income - expense).toLocaleString();
  }
  updateHeader();

  // — 월별 기록 로드 (/records?month=yyyy-MM-01) —
  async function loadMonthlyData() {
    const monthParam = `${yyyy}-${mm}-01`;
    console.log('Loading monthly data for', monthParam);
    try {
      const config = await getApiConfig();
      const res = await fetch(`${config.recordServiceUrl}/record/records?month=${monthParam}`, {
        headers: { 
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      console.log('GET /records status:', res.status);
      const text = await res.text();
      console.log('GET /records response:', text);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const list = JSON.parse(text);

      // 이번 달 수입/지출/잔고 합계 계산
      let incomeSum = 0, expenseSum = 0;
      list.forEach(r => {
        if (r.type === 'INCOME') incomeSum += r.amount;
        else if (r.type === 'EXPENSE') expenseSum += r.amount;
      });
      incomeEl.textContent = incomeSum.toLocaleString();
      expenseEl.textContent = expenseSum.toLocaleString();
      balanceEl.textContent = (incomeSum - expenseSum).toLocaleString();

      const events = list.map(r => ({
        title: `${r.type==='INCOME'?'+':'-'}${r.amount.toLocaleString()}원`,
        start: r.date,
        allDay: true,
        color: r.type === 'INCOME' ? '#1976d2' : '#d32f2f'
      }));
      calendar.removeAllEvents();
      calendar.addEventSource(events);

      window._lastMonthlyList = list;
    } catch (e) {
      console.error('월별 조회 에러:', e);
      if (e.message.includes('404')) {
        console.log('기록이 없습니다.');
      } else {
        alert('기록 로드 실패');
      }
    }
  }
  loadMonthlyData();

  // — 로그아웃 & 네비게이션 —
  document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    location.href = 'login.html';
  });
  document.getElementById('statsBtn').addEventListener('click', () => {
    location.href = 'stats.html';
  });
  document.getElementById('dashboardBtn').addEventListener('click', () => {
    alert('이미 대시보드입니다.');
  });

  // — 지출/수입 등록 처리 (/record POST) —
  const saveBtn = document.querySelector('.save-btn');
  console.log('save-btn element:', saveBtn);
  saveBtn.addEventListener('click', async () => {
    console.log('>>> save-btn clicked');
    const amtInput = document.querySelector(".input-section input[type='text']");
    const amt = parseInt(amtInput.value.trim(), 10);
    if (!amt) return alert('금액을 입력하세요.');

    const typeBtn = document.querySelector('.toggle-btn.active');
    if (!typeBtn) return alert('수입/지출 선택하세요.');
    const type = typeBtn.dataset.type === '수입' ? 'INCOME' : 'EXPENSE';

    const catBtn = document.querySelector('.category-btn.active');
    if (!catBtn) return alert('카테고리 선택하세요.');
    const category = catBtn.textContent;

    const memo = document.querySelector('.details textarea').value.trim();
    const date = dateInput.value;

    const recordDTO = { userId, amount: amt, type, category, memo, date };
    console.log('>>> about to POST recordDTO:', recordDTO);

    try {
      const config = await getApiConfig();
      const res = await fetch(`${config.recordServiceUrl}/record`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(recordDTO)
      });
      console.log('POST /record status:', res.status);
      const text = await res.text();
      console.log('POST /record response:', text);

      if (!res.ok) throw new Error(`HTTP ${res.status}: ${text}`);
      const created = JSON.parse(text);
      console.log('생성된 레코드:', created);

      // 로컬 업데이트
      if (type==='INCOME') {
        income += amt;
        localStorage.setItem('income', income);
      } else {
        expense += amt;
        localStorage.setItem('expense', expense);
      }
      updateHeader();

      // === 예산 초과 체크 ===
      // 1. 현재 월 예산 불러오기
      let budgetAmount = 0;
      try {
        const budgetConfig = await getApiConfig();
        const budgetRes = await fetch(`${budgetConfig.recordServiceUrl}/budget`, {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        if (budgetRes.ok) {
          const budgets = await budgetRes.json();
          const currentMonth = `${yyyy}-${mm}`;
          const currentBudget = budgets.find(b => b.month && b.month.substring(0, 7) === currentMonth);
          budgetAmount = currentBudget ? currentBudget.amount : 0;
        }
      } catch (e) {
        console.error('예산 조회 에러:', e);
      }
      // 2. expense(누적 지출)와 비교
      if (budgetAmount > 0 && expense > budgetAmount) {
        alert('⚠️ 예산을 초과했습니다!');
      }

      alert(`${type} 저장 완료 (ID: ${created.recId})`);
      loadMonthlyData();
    } catch (e) {
      console.error('저장 에러:', e);
      alert(`저장 실패: ${e.message}`);
    }
  });

  // — 토글/카테고리 버튼 UI —
  document.querySelectorAll('.toggle-btn').forEach(b => {
    b.addEventListener('click', () => {
      document.querySelectorAll('.toggle-btn').forEach(x => x.classList.remove('active'));
      b.classList.add('active');
    });
  });
  document.querySelectorAll('.category-btn').forEach(b => {
    b.addEventListener('click', () => {
      document.querySelectorAll('.category-btn').forEach(x => x.classList.remove('active'));
      b.classList.add('active');
    });
  });

  // 모달 생성 함수
  function showDetailModal(records, dateStr) {
    // 모달이 이미 있으면 제거
    let oldModal = document.getElementById('detail-modal');
    if (oldModal) oldModal.remove();

    // 모달 컨테이너
    const modal = document.createElement('div');
    modal.id = 'detail-modal';
    modal.style.position = 'fixed';
    modal.style.left = '0';
    modal.style.top = '0';
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.background = 'rgba(0,0,0,0.3)';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';
    modal.style.zIndex = '9999';

    // 모달 내용
    const content = document.createElement('div');
    content.style.background = '#fff';
    content.style.borderRadius = '20px';
    content.style.padding = '32px 24px';
    content.style.width = '400px';
    content.style.maxWidth = '90vw';
    content.style.boxShadow = '0 4px 24px rgba(0,0,0,0.15)';
    content.innerHTML = `
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <span style="font-size:1.2em;font-weight:bold;">${dateStr}</span>
        <span style="font-size:2em;cursor:pointer;" id="close-modal">&times;</span>
      </div>
      <hr style="margin:12px 0;" />
      <div id="modal-record-list"></div>
    `;
    modal.appendChild(content);
    document.body.appendChild(modal);

    // 닫기 버튼
    content.querySelector('#close-modal').onclick = () => modal.remove();

    // 상세내역 렌더링
    const listDiv = content.querySelector('#modal-record-list');
    if (records.length === 0) {
      listDiv.innerHTML = '<div style="text-align:center;color:#888;">기록 없음</div>';
    } else {
      // 수입/지출 분리
      const incomeList = records.filter(r => r.type === 'INCOME');
      const expenseList = records.filter(r => r.type === 'EXPENSE');
      let html = '';
      if (incomeList.length > 0) {
        html += '<div style="margin-bottom:16px;"><b>들어온 돈</b>';
        incomeList.forEach((r, idx) => {
          html += `
            <div class="record-row" data-recid="${r.recId}">
              <span style="font-size:1.5em;">💰</span>
              <span class="amount-income">${r.amount.toLocaleString()}</span>
              <span class="category">${r.category}</span>
              <button class="edit-btn moa-btn" style="margin-left:auto;">수정</button>
              <button class="delete-btn moa-btn" style="margin-left:6px;">삭제</button>
            </div>
            <div class="memo">메모 ${r.memo ? r.memo : ''}</div>
          `;
        });
        html += '</div>';
      }
      if (expenseList.length > 0) {
        html += '<div style="margin-bottom:16px;"><b>나간 돈</b>';
        expenseList.forEach((r, idx) => {
          html += `
            <div class="record-row" data-recid="${r.recId}">
              <span style="font-size:1.5em;">🚌</span>
              <span class="amount-expense">${r.amount.toLocaleString()}</span>
              <span class="category">${r.category}</span>
              <button class="edit-btn moa-btn" style="margin-left:auto;">수정</button>
              <button class="delete-btn moa-btn" style="margin-left:6px;">삭제</button>
            </div>
            <div class="memo">메모 ${r.memo ? r.memo : ''}</div>
          `;
        });
        html += '</div>';
      }
      listDiv.innerHTML = html;

      // 삭제 버튼 이벤트 연결
      listDiv.querySelectorAll('.delete-btn').forEach(btn => {
        btn.onclick = async function() {
          const row = btn.closest('.record-row');
          const recId = row.getAttribute('data-recid');
          if (!recId) return alert('레코드 ID 없음');
          if (!confirm('정말 삭제하시겠습니까?')) return;
          try {
            const res = await fetch(`${POST_URL}/${recId}`, {
              method: 'DELETE',
              headers: {
                'Authorization': `Bearer ${token}`
              }
            });
            if (!res.ok) throw new Error('삭제 실패');
            alert('삭제 완료!');
            document.getElementById('detail-modal').remove();
            loadMonthlyData();
          } catch (e) {
            alert('삭제 중 오류 발생: ' + e.message);
          }
        };
      });

      // 수정 버튼 이벤트 연결
      listDiv.querySelectorAll('.edit-btn').forEach(btn => {
        btn.onclick = function() {
          const row = btn.closest('.record-row');
          const recId = row.getAttribute('data-recid');
          const record = records.find(r => String(r.recId) === String(recId));
          if (!record) return alert('기록을 찾을 수 없습니다.');
          showEditModal(record, dateStr, records);
        };
      });
    }
  }

  // 수정 모달 함수
  function showEditModal(record, dateStr, allRecords) {
    let oldModal = document.getElementById('detail-modal');
    if (oldModal) oldModal.remove();
    // 모달 컨테이너
    const modal = document.createElement('div');
    modal.id = 'detail-modal';
    modal.style.position = 'fixed';
    modal.style.left = '0';
    modal.style.top = '0';
    modal.style.width = '100vw';
    modal.style.height = '100vh';
    modal.style.background = 'rgba(0,0,0,0.3)';
    modal.style.display = 'flex';
    modal.style.alignItems = 'center';
    modal.style.justifyContent = 'center';
    modal.style.zIndex = '9999';
    // 모달 내용
    const content = document.createElement('div');
    content.style.background = '#fff';
    content.style.borderRadius = '20px';
    content.style.padding = '32px 24px';
    content.style.width = '400px';
    content.style.maxWidth = '90vw';
    content.style.boxShadow = '0 4px 24px rgba(0,0,0,0.15)';
    content.innerHTML = `
      <div style="display:flex;justify-content:space-between;align-items:center;">
        <span style="font-size:1.2em;font-weight:bold;">${dateStr} 수정</span>
        <span style="font-size:2em;cursor:pointer;" id="close-modal">&times;</span>
      </div>
      <hr style="margin:12px 0;" />
      <div style="margin-bottom:12px;">
        <input id="edit-amount" type="number" value="${record.amount}" style="width:100%;padding:8px;font-size:1.1em;border-radius:6px;border:1px solid #ccc;" />
      </div>
      <div style="display:flex;gap:8px;margin-bottom:12px;">
        <button class="toggle-btn moa-btn${record.type==='EXPENSE'?' active':''}" data-type="지출">지출</button>
        <button class="toggle-btn moa-btn${record.type==='INCOME'?' active':''}" data-type="수입">수입</button>
      </div>
      <div class="category-section" style="margin-bottom:12px;">
        ${["💸 생활비","🚌 교통비","🍚 식비","💊 의료비","🛍️ 쇼핑","✈️ 여행","⚰️ 경조사","🔧 기타","💑 데이트","💰 수입","🛠️ 고정비"].map(cat=>`<button class="category-btn${record.category===cat.replace(/^[^ ]+ /,'')?' active':''}"><span>${cat}</span></button>`).join('')}
      </div>
      <textarea id="edit-memo" style="width:100%;height:60px;padding:8px;border-radius:6px;border:1px solid #ccc;">${record.memo||''}</textarea>
      <div style="display:flex;gap:8px;margin-top:18px;">
        <button id="edit-save-btn" class="moa-btn" style="flex:1;">저장</button>
        <button id="edit-cancel-btn" class="moa-btn" style="flex:1;background:#eee;color:#333;">취소</button>
      </div>
    `;
    modal.appendChild(content);
    document.body.appendChild(modal);
    // 닫기/취소 버튼
    content.querySelector('#close-modal').onclick = () => showDetailModal(allRecords, dateStr);
    content.querySelector('#edit-cancel-btn').onclick = () => showDetailModal(allRecords, dateStr);
    // 토글/카테고리 버튼 UI
    content.querySelectorAll('.toggle-btn').forEach(b => {
      b.addEventListener('click', () => {
        content.querySelectorAll('.toggle-btn').forEach(x => x.classList.remove('active'));
        b.classList.add('active');
      });
    });
    content.querySelectorAll('.category-btn').forEach(b => {
      b.addEventListener('click', () => {
        content.querySelectorAll('.category-btn').forEach(x => x.classList.remove('active'));
        b.classList.add('active');
      });
    });
    // 저장 버튼
    content.querySelector('#edit-save-btn').onclick = async () => {
      const amt = parseInt(content.querySelector('#edit-amount').value.trim(), 10);
      if (!amt) return alert('금액을 입력하세요.');
      const typeBtn = content.querySelector('.toggle-btn.active');
      if (!typeBtn) return alert('수입/지출 선택하세요.');
      const type = typeBtn.dataset.type === '수입' ? 'INCOME' : 'EXPENSE';
      const catBtn = content.querySelector('.category-btn.active');
      if (!catBtn) return alert('카테고리 선택하세요.');
      const category = catBtn.textContent.replace(/^[^ ]+ /,'');
      const memo = content.querySelector('#edit-memo').value.trim();
      try {
        const res = await fetch(`${POST_URL}/${record.recId}`, {
          method: 'PATCH',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify({
            userId: record.userId,
            amount: amt,
            type,
            category,
            memo,
            date: record.date
          })
        });
        if (!res.ok) throw new Error('수정 실패');
        alert('수정 완료!');
        document.getElementById('detail-modal').remove();
        loadMonthlyData();
      } catch (e) {
        alert('수정 중 오류 발생: ' + e.message);
      }
    };
  }
});
