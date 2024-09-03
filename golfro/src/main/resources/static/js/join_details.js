document.addEventListener('DOMContentLoaded', () => {
	const postIdElement = document.querySelector('input#postId');
	const btnDelete = document.querySelector('button#btnDelete');
	
	btnDelete.addEventListener('click', () => {
		const result = confirm('게시물을 삭제할까요?');
		if (result) {
			const postId = postIdElement.value; // 수정된 부분
			location.href = `delete?id=${postId}`;
		}
	});
});
