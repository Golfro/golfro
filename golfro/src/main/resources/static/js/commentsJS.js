/**
 *  mainPost/details 부여
 */

document.addEventListener('DOMContentLoaded', () => {
   getAllMainComments(); // 댓글 리스트업
   getAllLikes(); // 좋아요 수


   // 댓글 작성 스크립트
   const btnRegisterComment = document.querySelector('#btnRegisterComment');
   if (btnRegisterComment) {
      btnRegisterComment.addEventListener('click', registerComment);
   }
   
   
   const postAuthor = document.querySelector('#postAuthor').value;
   const sessionUser = document.querySelector('#sessionUser').value;
   const sessionNickName = document.querySelector('#sessionNickName').value;
   
   
   console.log(postAuthor);
   console.log(`세션 닉네임 : ${sessionNickName}`);
   
   
   
   
   
	// like 버튼(추천하기 버튼)
   const btnLikes = document.querySelector('button#likes');
   btnLikes.addEventListener('click', () => {
      if (sessionUser === postAuthor) {
         alert('본인의 게시물은 추천할 수 없습니다.');
      } else {
         const postId = document.querySelector('input#postId').value;
         const uri = `/mainPost/likes/${postId}`;

         axios.put(uri)
            .then((response) => {
               console.log(response);
               alert('해당 게시물 추천을 하였습니다.');
               getAllLikes();
            })
            .catch((error) => console.log(error));
      }
   });




   function getAllLikes() {
      const postId = document.querySelector('input#postId').value;
      const uri = `/mainPost/likes/${postId}`;

      axios.get(uri)
         .then((response) => {
            console.log(response);
            makeLikesElements(response.data);
         })
         .catch((error) => console.log(error))
   }



   function makeLikesElements(data) {
      const likesCounts = document.querySelector('p#likesCounts');
      likesCounts.textContent = ` ${data}`;
   }



	


   /* 댓글 작성 함수 */
   function registerComment() {
      const postId = document.querySelector('input#postId').value;
      const content = document.querySelector('textarea#content').value;
	  const userid = document.querySelector('#sessionUserId').value;
      
     

      if (content === '' || userid === '') {
         alert('피드백 내용 및 작성자는 필수 입력값입니다.')
         return;
      }

      // Ajax 요청에서 보낼 데이터 객체 생성
      const data = { postId, content, userid };
      console.log(data);

      axios
         .post('../api/mainComment', data)
         .then((response) => {
            console.log(response);
            if (response.data === 1) {
               alert('피드백 작성완료, 채택을 기다려주세요.');
               document.querySelector('textarea#content').value = '';
               //전체 댓글 갱신 함수넣기   
               getAllMainComments();

            }
         })
         .catch((error) => {
            console.log(error);
         });
   };


   function getAllMainComments() {
      const postId = document.querySelector('input#postId').value;
      const uri = `/mainPost/api/mainComment/all/${postId}`;

      axios
         .get(uri)
         .then((response) => {
            // selection === 1인 요소들을 최상단으로 정렬하고, 나머지는 modifiedTime을 기준으로 정렬
            const sortedData = response.data.sort((a, b) => {
               if (a.selection === 1 && b.selection !== 1) {
                  return -1; // a를 더 위로 올림
               } else if (a.selection !== 1 && b.selection === 1) {
                  return 1; // b를 더 위로 올림
               } else {
                  return b.modifiedTime - a.modifiedTime; // modifiedTime을 기준으로 내림차순 정렬
               }
            });

            console.log(sortedData);
            makeCommentElements(sortedData);

            if (response.data.some(comment => comment.selection === 1)) {
               hideAllSelectButtons();
            }
         })
         .catch((error) => {
            console.log(error);
         });

   };


   // 댓글 목을 전달 받아 html 작성
   function makeCommentElements(data) {
      const divComments = document.querySelector('div.comments-section');
      let htmlString = '';

      if (data === '' || data.length === 0) {
         htmlString = `<p id="nullComment">아직 피드백이 작성되지 않은 게시물입니다.</p>`;
      } else {
         for (let mainComment of data) {
            let highlightClass = mainComment.selection === 1 ? 'highlight' : '';

            htmlString += `
            <div class="comment ${highlightClass}" id="comment-${mainComment.id}">
               <div class="comment-thumb">
                  	<input type="hidden" class="imagePath" value="${mainComment.image}" />
			   		<img id="image-${mainComment.id}" class="image" src="" alt="Uploaded Image" />
               </div>


               <div class="comment-content">
                  <strong>${mainComment.nickName}</strong>
                  <div class="comment-text">
                     <p class="commentId d-none"></p>
                     <p>
                        <span>${mainComment.content}</span>
                     </p>
                  </div>
                  
                  <div class="button-container">${sessionUser === postAuthor ?
                  `<button class="btn selectComment" data-id="${mainComment.id}">채택</button>` : ''}
				  
                     ${sessionNickName === mainComment.nickName ?
                  `<button class="btn modifyComment" data-id="${mainComment.id}">수정</button>` : ''}
                     ${sessionNickName === mainComment.nickName ?
                  `<button class="btn deleteComment" data-id="${mainComment.id}">삭제</button>` : ''}
                  </div>
                  
                  <div class="edit-area mt-2" id="editMainConmment${mainComment.id}"
                     style="display: none;">
                     <textarea class="form-control" id="edit-textarea-${mainComment.id}">${mainComment.content}</textarea>
                     <div class="button-container">
                     <button class="btn mt-2 save-edit"
                        data-id="${mainComment.id}">저장</button>
                     <button class="btn mt-2 cancel-edit"
                        data-id="${mainComment.id}">취소</button>
                  </div></div>
               </div>
            </div>
               `;
         }
      }
      divComments.innerHTML = htmlString;

	  var imagePaths = document.querySelectorAll('.imagePath');
      var images = document.querySelectorAll('.image');
  
      // .imagePath와 .image를 순회하면서 각각의 요소에 대해 작업합니다.
      imagePaths.forEach(function(input, index) {
	      var file = input.value;
	      var imageUrl = '/user/file/image?file=' + encodeURIComponent(file);  // 이미지 파일명에 맞게 설정
	  
	      if (file) {
			fetch(imageUrl)
	        	.then(response => {
	            	if (!response.ok) {
	                	throw new Error('Network response was not ok.');
	             	}
	             	return response.blob();
	          	})
	          	.then(blob => {
	            	var reader = new FileReader();
	              	reader.onload = function() {
	               		// .image 요소를 올바르게 업데이트합니다.
	                  	if (images[index]) {
	                    	images[index].src = reader.result;
	                  	} else {
	                  		console.error('No image element found for index:', index);
	                	}
	               	};
	               	reader.readAsDataURL(blob);
	           	})
	          	.catch(error => {
	         		console.error('Error fetching image:', error);
	         	});
	  	} else {
	   		console.error('No file path found for index:', index);
	  	}
	});
	
	   const focusCommentId = document.querySelector('input#focusComment').value;

	   if (focusCommentId) {
		   const commentElement = document.getElementById(`comment-${focusCommentId}`);
		   if (commentElement) {
			   commentElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
		   } else {
			   console.warn(`Element with id 'comment-${focusCommentId}' not found.`);
		   }
	   } else {
		   console.error('focusCommentId is null or undefined.');
	   }

      /* 생선되는 모든 버튼에 이벤트 리스너를 추가하는 영역 */

      const selectComment = document.querySelectorAll('.selectComment');
      for (let selectButton of selectComment) {
         selectButton.addEventListener('click', selectComments);
      }


      const deleteButtons = document.querySelectorAll('.deleteComment');
      for (let btn of deleteButtons) {
         btn.addEventListener('click', deleteComments)
      }

      const modifyButtons = document.querySelectorAll('.modifyComment');
      for (let btn of modifyButtons) {
         btn.addEventListener('click', modifyMainComment);
      }


   }




   function modifyMainComment(event) {	
      const commentId = event.target.getAttribute('data-id');
      const commentTextElement = document.querySelector(`#comment-${commentId} .comment-text`);
      const editAreaElement = document.querySelector(`#comment-${commentId} .edit-area`);
      const buttonContainer = document.querySelector(`#comment-${commentId} .button-container`);
	
      alert('댓글을 수정합니다.');

      // 원래의 댓글 텍스트를 숨기고 수정 영역을 보여줍니다.
      commentTextElement.style.display = 'none';
      editAreaElement.style.display = 'block';

      // 수정 및 삭제 버튼 숨기기
      buttonContainer.style.display = 'none';

      // 저장 버튼 이벤트 리스너
      const saveButton = editAreaElement.querySelector('.save-edit');
      saveButton.addEventListener('click', () => {
         const editedContent = document.getElementById(`edit-textarea-${commentId}`).value;

         axios.put(`../api/mainComment/edit/${commentId}`, {id:commentId, content: editedContent })
            .then((response) => {
               alert('댓글이 수정되었습니다.');
               document.querySelector(`#comment-${commentId} .comment-text span`).textContent = editedContent;
               commentTextElement.style.display = 'block';
               editAreaElement.style.display = 'none';

               // 수정 및 삭제 버튼 다시 보이게 하기
               buttonContainer.style.display = 'flex';
            })
            .catch(error => console.log(error));
      });

      // 취소 버튼 이벤트 리스너
      const cancelButton = editAreaElement.querySelector('.cancel-edit');
      cancelButton.addEventListener('click', () => {
         commentTextElement.style.display = 'block';
         editAreaElement.style.display = 'none';

         // 수정 및 삭제 버튼 다시 보이게 하기
         buttonContainer.style.display = 'flex';
      });
   }




   function deleteComments(event) {
      console.log(event.target);
      const commentsId = event.target.getAttribute('data-id');
      const result = confirm('해당 피드백을 삭제하시겠습니까?');
      if (!result) return;

      const uri = `../api/mainComment/delete/${commentsId}`;
      axios.delete(uri)
         .then((response) => {
            console.log(response.data);
            if (response.data === 1) {
               alert('해당 피드백이 삭제되었습니다.');
               getAllMainComments();
            }
         })
         .catch((error) => console.log(error));
   }



   function selectComments(event) {
      console.log(event.target);
      const commentsId = event.target.getAttribute('data-id');
      const result = confirm('해당 피드백을 체택하시겠습니다? (채택 시 수정이 불가능하며 채택은 번복 할 수 없습니다.)');
      if (!result) {
         return;
      }
      const uri = `/mainPost/api/mainComment/selectComment/${commentsId}`;
	  axios.put(uri)
	      .then((response) => {
	          console.log(response.data);
	          alert('해당 게시물의 채택이 완료되었습니다.');
	          getAllMainComments();
	          alert('피드백을 작성해준 멘토에게 포인트가 지급되었습니다.');
	      })
	      .catch((error) => {
	          console.error('Error response:', error.response); // 응답 객체 전체를 출력
	          console.error('Error status:', error.response.status); // 상태 코드
	          console.error('Error data:', error.response.data); // 서버에서 반환한 에러 메시지
	          console.error('Error message:', error.message); // 기본 에러 메시지
	      });
   }







   function hideAllSelectButtons() {
      const content = document.querySelector('#content');
      const btnRegisterComment = document.querySelector('#btnRegisterComment');
      if (content) {
         content.style.backgroundColor = '#E7EFEE';
         content.value = '채택이 완료된 레슨입니다.';
         content.style.fontSize = '16px';
         content.readOnly = true;
         content.style.textAlign = 'center';
         btnRegisterComment.style.display = 'none';
         content.style.display = 'flex';
         content.style.justifyContent = 'center';
         content.style.alignItems = 'center';
         content.style.height = '80%'; // 높이를 부모 요소에 맞춰서 설정합니다.
         content.style.textAlign = 'center';
         content.style.paddingTop = '40px'; // 원하는 패딩 상단값을 설정
      }

      const finishContent = document.querySelector('#finishContent');
      if (finishContent) {
         finishContent.style.backgroundColor = '#E7EFEE';
         finishContent.value = '채택이 완료된 레슨입니다.';
         finishContent.style.fontSize = '16px';
         finishContent.readOnly = true;
         finishContent.style.display = 'flex';
         finishContent.style.justifyContent = 'center';
         finishContent.style.alignItems = 'center';
         finishContent.style.height = '70%'; // 높이를 부모 요소에 맞춰서 설정합니다.
         finishContent.style.textAlign = 'center';
         finishContent.style.paddingTop = '40px'; // 원하는 패딩 상단값을 설정
      }

      const modifyButton = document.querySelectorAll('.modifyComment');
      const selectButton = document.querySelectorAll('.selectComment');
      const deleteButton = document.querySelectorAll('.deleteComment');

      selectButton.forEach(button => {
         button.style.display = 'none';
      });
      deleteButton.forEach(button => {
         button.style.display = 'none';
      });
      modifyButton.forEach(button => {
         button.style.display = 'none';
      });

      const btnFoot = document.querySelector('#dropdownMenuButton');
      btnFoot.classList.add('d-none');

   }







});