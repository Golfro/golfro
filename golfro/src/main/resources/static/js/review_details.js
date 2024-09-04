document.addEventListener('DOMContentLoaded', () => {
	const postIdElement = document.querySelector('input#postId');
	const btnDeletePost = document.querySelector('button#btnDelete');
	const btnLikes = document.querySelector('button#btnLikes');
	const likesCountElement = document.querySelector('#likesCount');
	const btnRegisterComment = document.querySelector('button#btnRegisterComment');
	const commentList = document.querySelector('.comment-list');

	const btnLikesNotLoggedIn = document.getElementById('btnLikes-notloggedIn');
	let likedByCurrentUser = false; // 현재 사용자가 좋아요를 눌렀는지 여부를 추적

	// focusCommentId 체크
	const focusCommentIdElement = document.querySelector('input#commentId');
	if (focusCommentIdElement) {
		const focusCommentId = focusCommentIdElement.value;
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
	}

	// 게시물 삭제 버튼 클릭 이벤트 처리
	if (btnDeletePost) {
		btnDeletePost.addEventListener('click', () => {
			const postId = postIdElement.value;
			const result = confirm('게시물을 삭제하시겠습니까?');
			if (result) {
				fetch(`/gaebokchi/review/delete?id=${postId}`, {
					method: 'GET'
				})
					.then(response => {
						if (response.ok) {
							alert('게시물이 삭제되었습니다.');
							window.location.href = '/gaebokchi/review/review_main';
						} else {
							throw new Error('게시물 삭제에 실패했습니다.');
						}
					})
					.catch(error => {
						console.error('Error:', error);
					});
			}
		});
	}

	commentList.addEventListener('click', event => {
		if (event.target.id === 'btnDeleteComment') {
			const commentId = event.target.previousElementSibling.value;
			const result = confirm('댓글을 삭제하시겠습니까?');
			if (result) {
				fetch(`/gaebokchi/review/comments/${commentId}`, {
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

			event.target.disabled = true;

			const updateForm = document.createElement('form');
			updateForm.classList.add('update-form');
			updateForm.innerHTML = `
                <div class="input-group mb-3">
                    <textarea id="updatedContent" class="form-control" rows="2">${commentContent}</textarea>
                    <div class="input-group-append">
                        <button id="btnSubmitUpdate" class="btn btn-outline-secondary" type="button">수정 완료</button>
                        <button id="btnCancelUpdate" class="btn btn-outline-secondary ml-2" type="button">취소</button>
                    </div>
                </div>
            `;
			commentDiv.appendChild(updateForm);

			const btnCancelUpdate = updateForm.querySelector('#btnCancelUpdate');
			btnCancelUpdate.addEventListener('click', () => {
				updateForm.remove();
				event.target.disabled = false;
			});

			updateForm.querySelector('#btnSubmitUpdate').addEventListener('click', () => {
				const updatedContent = updateForm.querySelector('#updatedContent').value;
				const data = {
					id: commentId,
					content: updatedContent
				};

				fetch('/gaebokchi/review/comments', {
					method: 'PUT',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify(data)
				})
					.then(response => {
						if (response.ok) {
							alert('댓글이 수정되었습니다.');
							commentDiv.querySelector('p').textContent = updatedContent;
							updateForm.remove();
							event.target.disabled = false;
						} else {
							throw new Error('댓글 수정에 실패했습니다.');
						}
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
			window.location.href = '/gaebokchi/user/signin';
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
			fetch('/gaebokchi/review/increaseLikes?id=' + postId, {
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
					alert('추천은 한 게시물에 한 번만 가능합니다!');
				});
		});
	}

	// 댓글 등록 버튼 클릭 이벤트 처리
	if (btnRegisterComment) {
		btnRegisterComment.addEventListener('click', event => {
			event.preventDefault();

			const postId = postIdElement.value;
			const author = document.querySelector('input[name="author"]').value;
			const content = document.querySelector('textarea[name="content"]').value;

			if (content.trim() === '') {
				alert('댓글 내용을 입력하세요.');
				return;
			}

			const data = {
				postId: postId,
				author: author,
				content: content
			};

			fetch('/gaebokchi/review/comments', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify(data)
			})
				.then(response => {
					if (response.ok) {
						alert('댓글이 등록되었습니다.');
						location.reload();
					} else {
						throw new Error('댓글 등록에 실패했습니다.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
				});
		});
	}
});
