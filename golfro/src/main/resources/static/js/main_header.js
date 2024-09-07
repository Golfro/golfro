// 초기 설정: 페이지 로딩 시 헤더의 폰트 색상을 화이트로 설정
window.addEventListener('DOMContentLoaded', function() {
	const header = document.getElementById('헤더');
	const navItems = document.querySelectorAll('.nav-item a');
	const logoImage = document.querySelector('.logo img');
	const rightSectionButton = document.querySelector('.right-section button');
	const loginButton = document.querySelector('.login .login-button svg');
	const submenuItems = document.querySelectorAll('.submenu a'); // submenu 항목
 
	// 초기 설정: 헤더의 하단 테두리 숨기기
	header.style.borderBottom = 'none';
	header.style.backgroundColor = 'rgba(0, 0, 0, 0)'; // 투명 배경색 설정

	navItems.forEach(function(item) {
		item.style.color = 'white';
	});
	rightSectionButton.style.color = 'white';
	loginButton.style.fill = 'white';

	submenuItems.forEach(function(item) {
		item.style.color = 'initial'; // submenu 항목은 색상 변경 안 함
	});

	// 초기 설정: 로고 이미지 초기화
	logoImage.src = 'images/newlogo1_w.png';
});

window.addEventListener('scroll', function() {
	const header = document.getElementById('헤더');
	const navItems = document.querySelectorAll('.nav-item > a'); // submenu를 제외한 nav-item의 a 태그만 선택
	const submenuItems = document.querySelectorAll('.submenu a'); // submenu 항목

	const logoImage = document.querySelector('.logo img');
	const rightSectionButton = document.querySelector('.right-section button');
	const loginButton = document.querySelector('.login .login-button svg');

	if (window.scrollY > 720) {
		// 스크롤 위치가 720px 이상일 때
		header.style.backgroundColor = 'white';
		header.style.borderBottom = '1px solid #e4e4e4'; // 하단 테두리 추가
		navItems.forEach(function(item) {
			item.style.color = 'black'; // 네비게이션 텍스트 색상 변경
		});
		submenuItems.forEach(function(item) {
			item.style.color = 'initial'; // submenu 항목은 색상 변경 안 함
		});
		logoImage.src = 'images/newlogo1.png'; // 로고 이미지 변경
		rightSectionButton.style.color = 'black'; // 버튼 텍스트 색상 변경
		loginButton.style.fill = 'black'; // 로그인 버튼 아이콘 색상 변경 (SVG fill 속성)
	} else {
		// 스크롤 위치가 720px 미만일 때
		header.style.backgroundColor = 'rgba(0, 0, 0, 0)';
		header.style.borderBottom = 'none'; // 하단 테두리 제거
		navItems.forEach(function(item) {
			item.style.color = 'white'; // 네비게이션 텍스트 색상 화이트로 변경
		});
		submenuItems.forEach(function(item) {
			item.style.color = 'initial'; // submenu 항목은 색상 변경 안 함
		});
		logoImage.src = 'images/newlogo1_w.png'; // 기본 로고 이미지로 변경
		rightSectionButton.style.color = 'white'; // 버튼 텍스트 색상 화이트로 변경
		loginButton.style.fill = 'white'; // 로그인 버튼 아이콘 색상 화이트로 변경 (SVG fill 속성)
	}
});
