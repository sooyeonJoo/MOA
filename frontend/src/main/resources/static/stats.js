// stats.js

// API 설정 가져오기
let apiConfig = null;
async function getApiConfig() {
  if (!apiConfig) {
    const res = await fetch('/api/config');
    apiConfig = await res.json();
  }
  return apiConfig;
}

async function fetchBudget(userId, yyyy, mm, token) {
  try {
    const config = await getApiConfig();
    const res = await fetch(`${config.recordServiceUrl}/budget`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    if (!res.ok) throw new Error('예산 조회 실패');
    const budgets = await res.json();
    const currentMonth = `${yyyy}-${mm}`;
    const currentBudget = budgets.find(b => b.month && b.month.substring(0, 7) === currentMonth);
    return currentBudget ? currentBudget.amount : 0;
  } catch (e) {
    console.error('[stats.js] 예산 조회 에러:', e);
    return 0;
  }
}

document.addEventListener("DOMContentLoaded", async function () {
  const ctx = document.getElementById('categoryPieChart').getContext('2d');
  // 상단 상태 동기화
  const incomeEl = document.getElementById('income');
  const expenseEl = document.getElementById('expense');
  const balanceEl = document.getElementById('balance');
  const budgetAmountEl = document.getElementById('budgetAmount');
  const monthLabelEl = document.getElementById('budgetLabel');
  const openBudgetModalBtn = document.getElementById('openBudgetModalBtn');

  // 날짜 정보
  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, '0');
  const monthLabel = `${mm.replace(/^0/, '')}월`;
  const monthStr = `${yyyy}-${mm}`;
  console.log('[stats.js] monthStr:', monthStr);

  // 사용자 정보
  const userId = localStorage.getItem('userId');
  const token = localStorage.getItem('accessToken');
  console.log('[stats.js] userId:', userId, 'token:', token);

  // 예산 정보 불러오기 및 표시
  monthLabelEl.childNodes[0].textContent = `${monthLabel} 예산 : `;
  const budgetAmount = await fetchBudget(userId, yyyy, mm, token);
  budgetAmountEl.textContent = budgetAmount.toLocaleString() + '원';

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
        // 저장 후 예산 다시 불러오기
        const newBudget = await fetchBudget(userId, yyyy, mm, token);
        budgetAmountEl.textContent = newBudget.toLocaleString() + '원';
      } catch (e) {
        alert('예산 저장 중 오류: ' + e.message);
      }
    };
  }
  if (openBudgetModalBtn) {
    openBudgetModalBtn.addEventListener('click', showBudgetModal);
  }

  // 통계 데이터 불러오기
  let stats = [];
  try {
    const config = await getApiConfig();
    console.log('[stats.js] Fetching stats from API:', `${config.recordServiceUrl}/stats?month=${monthStr}`);
    const res = await fetch(`${config.recordServiceUrl}/stats?month=${monthStr}`, {
      headers: { 'Authorization': `Bearer ${token}` }
    });
    console.log('[stats.js] API response status:', res.status);
    if (!res.ok) throw new Error('통계 조회 실패');
    stats = await res.json();
    console.log('[stats.js] stats API response:', stats);
  } catch (e) {
    console.error('[stats.js] 통계 데이터를 불러오지 못했습니다:', e);
    stats = [];
  }

  // 차트 데이터 가공
  const labels = stats.map(s => s.category);
  const amounts = stats.map(s => s.amount);
  const totalIncome = stats.length > 0 ? stats[0].income : 0;
  const totalExpense = amounts.reduce((a, b) => a + b, 0);
  console.log('[stats.js] labels:', labels);
  console.log('[stats.js] amounts:', amounts);
  console.log('[stats.js] totalIncome:', totalIncome, 'totalExpense:', totalExpense);

  // 상단 상태 동기화
  incomeEl.textContent = totalIncome.toLocaleString();
  expenseEl.textContent = totalExpense.toLocaleString();
  balanceEl.textContent = (totalIncome - totalExpense).toLocaleString();
  console.log('[stats.js] incomeEl:', incomeEl.textContent, 'expenseEl:', expenseEl.textContent, 'balanceEl:', balanceEl.textContent, 'budgetAmountEl:', budgetAmountEl.textContent);

  // 차트 그리기
  console.log('[stats.js] Drawing chart with labels:', labels, 'amounts:', amounts);
  const pieChart = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        label: '카테고리별 지출',
        data: amounts,
        backgroundColor: [
          'rgba(215,179,221,0.85)',
          'rgba(197,138,201,0.85)',
          'rgba(181,105,194,0.85)',
          'rgba(158,61,168,0.85)',
          'rgba(128,45,134,0.85)',
          'rgba(103,25,108,0.85)'
        ],
        borderColor: '#fff',
        borderWidth: 3,
        hoverOffset: 16
      }]
    },
    options: {
      cutout: '65%',
      plugins: {
        legend: {
          display: true,
          position: 'bottom',
          labels: {
            usePointStyle: true,
            padding: 20,
            font: {
              size: 16,
              weight: 'bold'
            },
            generateLabels: function(chart) {
              const data = chart.data;
              const total = amounts.reduce((a, b) => a + b, 0);
              if (data.labels.length && data.datasets.length) {
                return data.labels.map((label, i) => {
                  const value = data.datasets[0].data[i];
                  const percent = total ? Math.round(value / total * 100) : 0;
                  const bgColor = data.datasets[0].backgroundColor[i];
                  console.log(`[stats.js] legend: ${label} ${value} (${percent}%)`);
                  return {
                    text: `${label}  ${value.toLocaleString()}원 (${percent}%)`,
                    fillStyle: bgColor,
                    strokeStyle: bgColor,
                    index: i
                  };
                });
              }
              return [];
            }
          }
        },
        title: {
          display: true,
          text: `${monthStr} 카테고리별 소비 금액`,
          font: {
            size: 22,
            weight: 'bold'
          },
          color: '#6a4fb3',
          padding: {
            top: 10,
            bottom: 20
          }
        },
        tooltip: {
          backgroundColor: '#fff',
          titleColor: '#6a4fb3',
          bodyColor: '#333',
          borderColor: '#bba6d9',
          borderWidth: 1,
          padding: 12,
          callbacks: {
            label: function(context) {
              const label = context.label || '';
              const value = context.parsed || 0;
              const percent = totalExpense ? Math.round(value / totalExpense * 100) : 0;
              console.log(`[stats.js] tooltip: ${label} ${value} (${percent}%)`);
              return `${label}: ${value.toLocaleString()}원 (${percent}%)`;
            }
          }
        }
      },
      animation: {
        animateRotate: true,
        animateScale: true
      }
    },
    plugins: [{
      id: 'centerText',
      afterDraw: function(chart) {
        const {ctx, chartArea: {width, height}} = chart;
        ctx.save();
        ctx.font = 'bold 22px Segoe UI';
        ctx.fillStyle = '#6a4fb3';
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.fillText(`${monthStr}`, width / 2, height / 2 - 16);
        ctx.font = 'bold 16px Segoe UI';
        ctx.fillText(`총수입: ${totalIncome.toLocaleString()}원`, width / 2, height / 2 + 16);
        ctx.restore();
      }
    }]
  });

  // 로그아웃 버튼
  document.getElementById('logoutBtn').addEventListener('click', () => {
    localStorage.clear();
    location.href = 'login.html';
  });
});