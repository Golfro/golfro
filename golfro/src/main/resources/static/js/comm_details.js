document.addEventListener('DOMContentLoaded', () => {
	console.log('커뮤니티 상세보기');

	const postIdElement = document.querySelector('input#postId');
	const btnDeletePost = document.querySelector('button#btnDelete');
	
	const likesCountElement = document.querySelector('#likesCount');
	const btnRegisterComment = document.querySelector('button#btnRegisterComment');
	const commentList = document.querySelector('.comment-list');
	
	
	const btnLikes = document.querySelector('button#btnLikes');
	const btnLikesNotLoggedIn = document.getElementById('btnLikes-notloggedIn');
	let likedByCurrentUser = false; // 현재 사용자가 좋아요를 눌렀는지 여부를 추적

	/*	const focusCommentId = document.querySelector('input#commentId').value;
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
		
		*/

	// 게시물 삭제 버튼 클릭 이벤트 처리
	if (btnDeletePost) {
		btnDeletePost.addEventListener('click', () => {
			const id = postIdElement.value;
			const result = confirm('게시물을 삭제하시겠습니까?');
			if (result) {
				fetch(`/community/delete/${id}`, {
					method: 'GET'
				})
					.then(response => {
						if (response.ok) {
							alert('게시물이 삭제되었습니다.');
							window.location.href = '/community/comm_main';
						} else {
							throw new Error('게시물 삭제에 실패했습니다.');
						}
					})
					.catch(error => {
						console.error('Error:', error);
					});
			} else {

				window.location.href = `/community/details/${id}`; // 취소 후 이동할 페이지

			}
		});
	}

	commentList.addEventListener('click', event => {
		if (event.target.id === 'btnDeleteComment') {
			const commentId = event.target.previousElementSibling.value;
			const result = confirm('댓글을 삭제하시겠습니까?');
			if (result) {
				fetch(`/community/comments/${commentId}`, {
					method: 'DELETE'
				})
					.then(response => {
						if (response.ok) {
							alert('댓글이 삭제되었습니다.');
							event.target.closest('.comment').remove();
						} else {
							throw new Error('댓글 삭제에 실패했습니다.');
						}
					})
					.catch(error => {
						console.error('Error:', error);
					});
			}
		} else if (event.target.id === 'btnUpdateComment') {
			const commentDiv = event.target.closest('.comment');
			const commentId = commentDiv.querySelector('input[name="commentId"]').value;
			const commentContent = commentDiv.querySelector('p').textContent;

			commentDiv.querySelector('.mainComment').classList.add('hidden');
			event.target.disabled = true;

			const updateForm = document.createElement('form');
			updateForm.classList.add('update-form');
			updateForm.innerHTML = `
                <div class="input-group-modify row px-3 py-1">
                    <textarea id="updatedContent" class="form-control col-12 mt-2 mx-2" rows="3">${commentContent}</textarea>
                    <div class="d-flex justify-content-end">
                 	   <div class="input-group-append mt-2">
                      	    <button id="btnSubmitUpdate" class="btn" type="button">수정 완료</button>
                    		<button id="btnCancelUpdate" class="btn ml-2" type="button">취소</button>
                   		 </div>
                   	</div>
                </div>
            `;
			commentDiv.appendChild(updateForm);

			const btnCancelUpdate = updateForm.querySelector('#btnCancelUpdate');
			btnCancelUpdate.addEventListener('click', () => {
				updateForm.remove();

				commentDiv.querySelector('.mainComment').classList.remove('hidden'); // 수정된 부분!!!
				event.target.disabled = false;
				event.target.disabled = false;
			});

			updateForm.querySelector('#btnSubmitUpdate').addEventListener('click', () => {
				const updatedContent = updateForm.querySelector('#updatedContent').value;
				const data = {
					commentId: commentId,
					content: updatedContent
				};

				fetch('/community/comments', {
					method: 'PUT',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(data)
				})
					.then(response => {
						if (response.ok) {
							return response.json();  // 응답을 JSON으로 파싱
						} else {
							throw new Error('댓글 수정에 실패했습니다.');
						}
					})
					.then(updatedComment => {
						alert('댓글이 수정되었습니다.');
						commentDiv.querySelector('p').textContent = updatedComment.content;  // 서버에서 받은 수정된 댓글 내용 사용
						updateForm.remove();

						commentDiv.querySelector('.mainComment').classList.remove('hidden'); // 수정된 부분!!!
						event.target.disabled = false;
						event.target.disabled = false;
					})
					.catch(error => {
						console.error('Error:', error);
					});
			});
		}
	});



	// 버튼 클릭 이벤트 처리
	if (btnLikesNotLoggedIn) {
		btnLikesNotLoggedIn.addEventListener('click', () => {
			// 알림 메시지 출력
			alert('추천은 로그인 후 가능합니다.');

			// 로그인 페이지로 리디렉션
			window.location.href = '/user/signin';
		});
	}


	// 좋아요 버튼 클릭 이벤트 처리
	if (btnLikes) {
		btnLikes.addEventListener('click', () => {
			if (likedByCurrentUser) {
				alert('이미 좋아요를 누르셨습니다.');
				return;
			}

			const postId = postIdElement.value;
			fetch('/community/increaseLikes?id=' + postId, {
				method: 'POST',
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded'
				}
			})
				.then(response => {
					if (response.ok) {
						return response.json();
					} else {
						throw new Error('Failed to increase likes');
					}
				})
				.then(data => {
					likedByCurrentUser = true; // 좋아요를 눌렀음을 표시
					likesCountElement.textContent = data.likes;
					alert('게시물에 좋아요를 눌렀습니다.');
				})
				.catch(error => {
					console.error('Error:', error);
					alert('이미 추천한 게시물입니다!');
				});
		});
	}

	// 댓글 등록 버튼 클릭 이벤트 처리
	if (btnRegisterComment) {
		btnRegisterComment.addEventListener('click', event => {
			event.preventDefault();

			const postId = document.querySelector('#postId').value;
			console.log(`postid = ${postId}`);

			const author = document.querySelector('#author').value;
			console.log(`author = ${author}`);

			const content = document.querySelector('#content').value;
			console.log(`content = ${content}`);

			if (content.trim() === '') {
				alert('댓글 내용을 입력하세요.');
				return;
			}

			const data = {
				postId: postId,
				userid: author,
				content: content
			};



			fetch('/community/comments', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			})
				.then(response => {
					if (response.ok) {
						return response.text();  // JSON 대신 텍스트로 응답 처리
					} else {
						throw new Error('댓글 등록에 실패했습니다.');
					}
				})
				.then(text => {
					if (text) {
						return JSON.parse(text);  // 텍스트가 있으면 JSON으로 파싱
					}
					throw new Error('서버에서 빈 응답을 받았습니다.');
				})
				.then(newComment => {
					console.log('New Comment:', newComment);
					displayComment(newComment);
					alert('댓글이 등록되었습니다.');
					document.querySelector('input#author').value = '';
					document.querySelector('textarea#content').value = '';
					location.reload();
				})
				.catch(error => {
					console.error('Error:', error);
					alert(error.message);  // 에러 메시지를 사용자에게 표시
				});

		});
	}

	function displayComment(comment) {
		const commentDiv = document.createElement('div');
		commentDiv.classList.add('comment');
		commentDiv.innerHTML = `
            <b>${comment.author}</b>
            <p>${comment.content}</p>
            <small>${comment.modifiedTime ? new Date(comment.modifiedTime).toLocaleString() : '시간 없음'}</small>
            <input type="hidden" name="commentId" value="${comment.id}">
            <button class="btn btn-outline-success btnUpdateComment">수정</button>
            <button class="btn btn-outline-danger btnDeleteComment">삭제</button>
        `;
		commentList.appendChild(commentDiv);
	}

});