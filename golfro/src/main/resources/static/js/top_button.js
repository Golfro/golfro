document.addEventListener('DOMContentLoaded', function() {

	var topBtn = document.createElement('button');
	topBtn.id = 'topBtn';
	topBtn.title = 'Go to top';
	topBtn.textContent = 'Top';
	topBtn.style.display = 'none';
	topBtn.style.position = 'fixed';
	topBtn.style.bottom = '80px';
	topBtn.style.right = '30px';
	topBtn.style.zIndex = '99';
	topBtn.style.border = '2px solid black';
	topBtn.style.outline = 'none';
	topBtn.style.backgroundColor = '#fff';
	topBtn.style.color = 'black';
	topBtn.style.cursor = 'pointer';
	topBtn.style.padding = '10px';
	topBtn.style.borderRadius = '10px';

	var styleTopBtn = document.createElement('style');
	styleTopBtn.textContent = `
        #topBtn:hover {
            background-color: #333;
        }
    `;
	document.head.appendChild(styleTopBtn);

	// 탑 버튼 클릭 이벤트 핸들러
	topBtn.addEventListener('click', function() {
		document.body.scrollTop = 0;
		document.documentElement.scrollTop = 0;
	});

	// 탑 버튼을 body에 추가
	document.body.appendChild(topBtn);




	// 챗봇 버튼 HTML 생성
	var chatBtn = document.createElement('button');
	chatBtn.id = 'chatBtn';
	chatBtn.title = '챗봇과 대화하기';
	chatBtn.innerHTML = '<i class="bi bi-chat-dots"></i>';
	chatBtn.style.position = 'fixed';
	chatBtn.style.bottom = '20px'; // 탑 버튼 아래에 배치
	chatBtn.style.right = '30px';
	chatBtn.style.zIndex = '100';
	chatBtn.style.border = 'none';
	chatBtn.style.outline = 'none';
	chatBtn.style.backgroundColor = '#007bff';
	chatBtn.style.color = 'white';
	chatBtn.style.cursor = 'pointer';
	chatBtn.style.padding = '15px'; 
	chatBtn.style.width = '53px'; // 버튼의 너비
	chatBtn.style.height = '53px'; // 버튼의 높이
	chatBtn.style.borderRadius = '50%';
	chatBtn.style.boxShadow = '0 4px 8px rgba(0,0,0,0.2)';
	chatBtn.style.fontSize = '24px';
	chatBtn.style.display = 'flex';
	chatBtn.style.alignItems = 'center';
	chatBtn.style.justifyContent = 'center';

	// 챗봇 버튼 스타일 추가
	var styleChatBtn = document.createElement('style');
	styleChatBtn.textContent = `
        #chatBtn:hover {
            background-color: #0056b3;
        }
    `;
	document.head.appendChild(styleChatBtn);

	// 챗봇 버튼 클릭 이벤트 핸들러
	chatBtn.addEventListener('click', function() {
		window.open('/chatbot.html', '_blank', 'width=400,height=600,left=100,top=100');
	});

	// 챗봇 버튼을 body에 추가
	document.body.appendChild(chatBtn);

	// 스크롤에 따른 탑 버튼 표시/숨김
	window.onscroll = function() {
		var topBtn = document.getElementById("topBtn");
		if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
			topBtn.style.display = "block";
		} else {
			topBtn.style.display = "none";
		}
	};
});
