/**
 * 
 */

document.addEventListener('DOMContentLoaded', () => {
   
   const postId = document.querySelector('#postId').value;

   const btnDeleteMainPost = document.querySelector('button.btnDeleteMainPost');
   btnDeleteMainPost.addEventListener('click', () => {
      const result = confirm('게시물을 삭제하시겠습니까?');
      if (result) {
         location.href = `delete?id=${postId}`;
      }
   })

   /* 게시글 수정 스크립트 */
   const btnModifyid = document.querySelector('button.btnModifyid');
   btnModifyid.addEventListener('click', () => {
      const result = confirm('게시물을 수정 하시겠습니까?');
      if (result) {
         location.href = `modify?id=${postId}`;
      }
   })
   
})