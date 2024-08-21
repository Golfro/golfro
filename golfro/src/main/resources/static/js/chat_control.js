/**
 * index.html에 포함.
 */
document.getElementById('send-button').addEventListener('click', async () => {
	const inputField = document.getElementById('user-input');
	const userMessage = inputField.value.trim();

	if (userMessage) {
		appendMessage(userMessage, 'user');
		inputField.value = '';

		try {
			const response = await fetch('/chat', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({ message: userMessage }),
			});

			if (response.ok) {
				const data = await response.text();
				appendMessage(data, 'ai');
			} else {
				appendMessage('서버 오류 발생', 'ai');
			}
		} catch (error) {
			console.error('통신 오류:', error);
			appendMessage('통신 오류 발생', 'ai');
		}
	}
});

// 엔터 키를 눌렀을 때 전송 버튼 클릭 이벤트 발생
document.getElementById('user-input').addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
        event.preventDefault(); // 기본 엔터 키 동작을 방지 (예: 줄 바꿈)
        document.getElementById('send-button').click(); // 전송 버튼 클릭 이벤트 트리거
    }
});

function appendMessage(message, sender) {
	const messagesDiv = document.getElementById('messages');
	const messageDiv = document.createElement('div');
	messageDiv.className = `message ${sender}`;
	messageDiv.textContent = message;
	messagesDiv.appendChild(messageDiv);
	messagesDiv.scrollTop = messagesDiv.scrollHeight; // 스크롤을 최신 메시지로 이동
}