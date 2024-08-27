// 초기 설정: 페이지 로딩 시 헤더의 폰트 색상을 화이트로 설정
window.addEventListener('DOMContentLoaded', function() {
    const header = document.getElementById('헤더');
    const navItems = document.querySelectorAll('.nav-item span');
    const logoImage = document.querySelector('.logo img');
    const rightSectionButton = document.querySelector('.right-section button');
    const loginButton = document.querySelector('.login .login-button svg');

    // 초기 설정: 헤더의 하단 테두리 숨기기
    header.style.borderBottom = 'none';
    header.style.backgroundColor = 'rgba(0, 0, 0, 0)'; // 투명 배경색 설정

    navItems.forEach(function(item) {
        item.style.color = 'white';
    });
    rightSectionButton.style.color = 'white';
    loginButton.style.fill = 'white';

    // 초기 설정: 로고 이미지 초기화
    logoImage.src = 'images/newlogo1_w.png';
});

// 스크롤 이벤트: 스크롤을 내려서 특정 위치에 도달하면 색상 변경 및 로고 이미지 변경
window.addEventListener('scroll', function() {
    const header = document.getElementById('헤더');
    const navItems = document.querySelectorAll('.nav-item span');
    const logoImage = document.querySelector('.logo img');
    const rightSectionButton = document.querySelector('.right-section button');
    const loginButton = document.querySelector('.login .login-button svg');

    if (window.scrollY > 960) {
        // 스크롤 위치가 960px 이상일 때
        header.style.backgroundColor = 'white';
        header.style.borderBottom = '1px solid #e4e4e4'; // 하단 테두리 추가
        navItems.forEach(function(item) {
            item.style.color = 'black'; // 네비게이션 텍스트 색상 변경
        });
        logoImage.src = 'images/newlogo1.png'; // 로고 이미지 변경
        rightSectionButton.style.color = 'black'; // 버튼 텍스트 색상 변경
        loginButton.style.fill = 'black'; // 로그인 버튼 아이콘 색상 변경 (SVG fill 속성)
    } else {
        // 스크롤 위치가 960px 미만일 때
        header.style.backgroundColor = 'rgba(0, 0, 0, 0)';
        header.style.borderBottom = 'none'; // 하단 테두리 제거
        navItems.forEach(function(item) {
            item.style.color = 'white'; // 네비게이션 텍스트 색상 화이트로 변경
        });
        logoImage.src = 'images/newlogo1_w.png'; // 기본 로고 이미지로 변경
        rightSectionButton.style.color = 'white'; // 버튼 텍스트 색상 화이트로 변경
        loginButton.style.fill = 'white'; // 로그인 버튼 아이콘 색상 화이트로 변경 (SVG fill 속성)
    }
});

