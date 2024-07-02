    function selectGender(button) {
        document.querySelectorAll('.form_item input[type="button"]').forEach(btn => btn.classList.remove('active'));
        button.classList.add('active');
    }

    const birthYearEl = document.querySelector('#birth-year')
// option 목록 생성 여부 확인
isYearOptionExisted = false;
birthYearEl.addEventListener('focus', function () {
// year 목록 생성되지 않았을 때 (최초 클릭 시)
if(!isYearOptionExisted) {
isYearOptionExisted = true
for(var i = 1940; i <= 2024; i++) {
  // option element 생성
  const YearOption = document.createElement('option')
  YearOption.setAttribute('value', i)
  YearOption.innerText = i
  // birthYearEl의 자식 요소로 추가
  this.appendChild(YearOption);
}
}
});

const birthMonthEl = document.querySelector('#birth-month')
// option 목록 생성 여부 확인
isMonthOptionExisted = false;
birthMonthEl.addEventListener('focus', function () {
// year 목록 생성되지 않았을 때 (최초 클릭 시)
if(!isMonthOptionExisted) {
isMonthOptionExisted = true
for(var i = 1; i <= 12; i++) {
  // option element 생성
  const MonthOption = document.createElement('option')
  MonthOption.setAttribute('value', i)
  MonthOption.innerText = i
  // birthYearEl의 자식 요소로 추가
  this.appendChild(MonthOption);
}
}
});

const birthDayEl = document.querySelector('#birth-day')
// option 목록 생성 여부 확인
isDayOptionExisted = false;
birthDayEl.addEventListener('focus', function () {
// year 목록 생성되지 않았을 때 (최초 클릭 시)
if(!isDayOptionExisted) {
isDayOptionExisted = true
for(var i = 1; i <= 31; i++) {
  // option element 생성
  const DayOption = document.createElement('option')
  DayOption.setAttribute('value', i)
  DayOption.innerText = i
  // birthYearEl의 자식 요소로 추가
  this.appendChild(DayOption);
}
}
});

    // 회원 정보 저장 버튼 클릭 시
    document.getElementById('saveProfileButton').addEventListener('click', function() {
        // 여기에 회원 정보 저장하는 로직을 추가할 수 있습니다.
        // AJAX 요청 등을 통해 서버에 정보를 전달하고, 성공적으로 저장되었다는 응답을 받으면 모달을 엽니다.
        
        // 예시로 모달을 바로 열도록 구현하겠습니다.
        openModal();
    });

    // 모달 창 열기
    function openModal() {
        var modal = document.getElementById("myModal");
        modal.style.display = "block";
    }

    // 모달 닫기 버튼 클릭 시 모달 창 닫기
    document.getElementById('closeModal').addEventListener('click', function() {
        document.getElementById('myModal').style.display = 'none';
    });

    // 확인 버튼 클릭 시 마이페이지로 이동
    document.getElementById('modalMyPageButton').addEventListener('click', function() {
        window.location.href = 'mypage.html';
    });

    // 모달 외부를 클릭했을 때 모달 창 닫기
    window.addEventListener('click', function(event) {
        if (event.target === document.getElementById('myModal')) {
            document.getElementById('myModal').style.display = 'none';
        }
    });