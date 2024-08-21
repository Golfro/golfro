/**
 * FAQ Chat Bot
 */

const responses = {
    '안녕': '안녕하세요! 어떻게 도와드릴까요?',
    '날씨': '오늘의 날씨는 맑고 따뜻합니다.',
    '이름': '저는 당신의 친절한 AI 어시스턴트입니다.',
    '기본': '저는 다양한 질문에 답할 수 있습니다. 어떤 도움이 필요하신가요?',
    '시작': '안녕하세요! 선택사항을 클릭하세요👇'
};

function displayOptions() {
    const options = [
        { text: '1. 옵션 1', action: 'option1' },
        { text: '2. 옵션 2', action: 'option2' },
        { text: '3. 옵션 3', action: 'option3' }
    ];
    const messagesDiv = document.getElementById('messages');
    const optionElements = options.map(option => {
        const button = document.createElement('button');
        button.classList.add('button-option');
        button.textContent = option.text;
        button.onclick = () => handleOption(option.action);
        return button;
    });
    optionElements.forEach(button => messagesDiv.appendChild(button));
}

async function chat(prompt) {
    try {
        let response;
		if (prompt === 'start') {
			// `responses['시작']` 메시지를 추가
			response = responses['시작'];
			appendMessage(response, 'ai'); // 'ai'로 메시지를 추가합니다.

			// `responses['시작']` 메시지 추가 후 옵션 버튼을 표시
			setTimeout(() => displayOptions(), 500); // 잠시 지연 후 옵션 버튼 표시
			return 200; // 성공 코드
		}
		
        response = responses[prompt] || responses['기본'];

        if (response) {
            appendMessage(response, 'ai');
            return 200; // 성공 코드
        } else {
            console.error('응답을 찾을 수 없습니다.');
            return 500; // 실패 코드
        }
    } catch (error) {
        console.error(`서버 에러가 발생했습니다: ${error}`);
        return 500; // 실패 코드
    }
}

function handleOption(option) {
    const messagesDiv = document.getElementById('messages');
    const responseDiv = document.createElement('div');
    responseDiv.className = 'message ai';
    
    switch (option) {
        case 'option1':
            responseDiv.textContent = '옵션 1을 선택하셨습니다. 추가 정보는 다음과 같습니다...';
            break;
        case 'option2':
            responseDiv.textContent = '옵션 2를 선택하셨습니다. 추가 정보는 다음과 같습니다...';
            break;
        case 'option3':
            responseDiv.textContent = '옵션 3을 선택하셨습니다. 추가 정보는 다음과 같습니다...';
            break;
        default:
            responseDiv.textContent = '유효하지 않은 옵션입니다. 다시 시도해 주세요.';
            break;
    }
    
    messagesDiv.appendChild(responseDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight; // 스크롤을 최신 메시지로 이동
}

function appendMessage(message, sender) {
    const messagesDiv = document.getElementById('messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${sender}`;
    messageDiv.textContent = message;
    messagesDiv.appendChild(messageDiv);
    messagesDiv.scrollTop = messagesDiv.scrollHeight; // 스크롤을 최신 메시지로 이동
}

// 실행
chat('start');
