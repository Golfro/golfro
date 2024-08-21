/**
 * index.html에 포함.
 */
// Import library
const {GoogleGenerativeAI} = require('@google/generative-ai');

// API key
const apiKey = 'AIzaSyD2ZzaMwoM0Y6J7GJHBDsBg1WBYcdgyS7A';
const gemini = new GoogleGenerativeAI(apiKey);

// Run
async function chat(prompt){
    try{
        const model = gemini.getGenerativeModel({model: 'gemini-pro'});
        const result = await model.generateContent(prompt);
        const response = await result.response;
        const text = await response.text();

        if (text) {
            console.log(text);
            return 200
        }
    }

    catch (error){
        console.error(`서버 에러가 발생했습니다: ${error}`);
    }
}
 
chat('안녕');