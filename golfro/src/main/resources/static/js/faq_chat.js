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
        { text: '1. GOLFRO란?', action: 'option1' },
        { text: '2. 게시판 이용 방법', action: 'option2' },
        { text: '3. 포인트 획득과 환전', action: 'option3' }
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
            responseDiv.textContent = 'GOLFRO란, Golf + Professional의 합성어로 저희 사이트가 골프 프로선수가 되기 위한 첫걸음이 되었으면 하는 바람을 담았습니다.';
            break;
        case 'option2':
            responseDiv.innerHTML = `1. 레슨: 일반회원이 자신의 골프 영상을 업로드하면 프로회원이 영상에 댓글로 피드백을 해주는 형식으로 온라인 강습장과 같은 효과를 만들어냅니다.<br>
                                     2. 모임: 회원들과의 오프라인 라운딩 모임을 약속할 수 있습니다.<br>
                                     3. 리뷰: 사이트를 이용하면서 경험했던 후기를 공유해 주세요.<br>
                                     4. 커뮤니티: 회원들과 다양한 주제로 대화하며 친목을 쌓아보세요. 새로운 인연들을 만날 수 있습니다!`;
            break;
        case 'option3':
            responseDiv.textContent = '포인트는 레슨 게시판에 프로 회원이 댓글로 피드백을 달고 채택이 돼야만 획득할 수 있습니다. 그동안 모았던 포인트는 마이페이지의 포인트 아이콘을 클릭하여 환전 페이지에서 원하는 만큼의 포인트를 자신의 계좌로 환전이 가능합니다.';
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
