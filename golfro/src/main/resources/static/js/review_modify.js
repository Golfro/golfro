document.addEventListener('DOMContentLoaded', () => {
   const modifyForm = document.querySelector('form#modifyForm');
   const inputId = document.querySelector('input[name=id]');
   const inputTitle = document.querySelector('input[name=title]');
   const inputCategory = document.querySelector('select[name=category]');
   const textContent = document.querySelector('textarea[name=content]');
   const btnUpdate = document.querySelector('button#btnUpdate');

   // 제목, 내용 길이 체크 이벤트 리스너
   inputTitle.addEventListener('input', function() {
      if (inputTitle.value.trim().length > 20) {
         alert('제목은 20자 이내로 입력해주세요!');
         inputTitle.value = inputTitle.value.trim().substring(0, 20);
      }
   });

   btnUpdate.addEventListener('click', () => {
      // 업데이트 내용 저장 확인:
      const result = confirm('변경 내용을 저장할까요?');
      if (result) {
         // Quill의 내용이 textarea에 반영되도록 함
         textContent.value = quill.root.innerHTML;
         modifyForm.action = 'update'; // 요청 주소
         modifyForm.method = 'POST'; // 요청 방식
         modifyForm.submit(); // 폼 양식 데이터 제출(서버로 요청 보냄).
      }
   });
});