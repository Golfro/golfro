/**
 * AI Chat Bot
 */
// Import library
const { GoogleGenerativeAI } = require('@google/generative-ai');

// API key
const apiKey = 'AIzaSyD2ZzaMwoM0Y6J7GJHBDsBg1WBYcdgyS7A';
const gemini = new GoogleGenerativeAI(apiKey);

// 명령줄 인자로부터 프롬프트를 가져옵니다.
const prompt = process.argv[2];

// Run
async function chat(prompt){
    try{
        const model = gemini.getGenerativeModel({model: 'gemini-pro'});
        const result = await model.generateContent(prompt);
        const response = await result.response;
        const text = await response.text();

		if (text) {
			console.log(text);  // 로그에 응답 내용을 출력하여 확인
			return text;
		} else {
			console.error('빈 응답');
			return '빈 응답이 반환되었습니다.';
		}
    }

    catch (error){
        console.error(`서버 에러가 발생했습니다: ${error}`);
    }
}

// 프롬프트가 제공되지 않은 경우, 에러 메시지를 반환
if (prompt) {
    chat(prompt);
} else {
    console.error('프롬프트가 제공되지 않았습니다.');
}