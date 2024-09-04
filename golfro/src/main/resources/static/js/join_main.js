/**
 * join_main.html
 */
document.addEventListener('DOMContentLoaded', function() {
	// 모든 .imagePath와 .image 클래스를 가진 요소를 찾습니다.
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

	const todayDateStr = document.getElementById("todayDate").value;
	const todayDate = new Date(todayDateStr);

	document.querySelectorAll('.card-list').forEach(card => {
		const teeoffDateStr = card.getAttribute('data-teeoff');
		const teeoffDate = new Date(teeoffDateStr);
		if (teeoffDate < todayDate) {
			card.classList.add('bg-light');
		}
	});

    updateFieldsBasedOnCategory(); // 페이지 로드 시 현재 선택된 카테고리에 따라 필드 초기화
    document.getElementById('category').addEventListener('change', updateFieldsBasedOnCategory);
	
	// 함수 정의
    function updateFieldsBasedOnCategory() {
        var selectedCategory = document.getElementById('category').value;
        var holeField = document.getElementById('holeField');
        var keywordField = document.getElementById('keywordField');

        if (selectedCategory === 'h') {
            holeField.classList.remove('d-none'); // 체크박스 그룹 보이기
            keywordField.classList.add('d-none'); // 검색어 입력 필드 숨기기
			document.getElementById('keyword').value = '';
        } else {
            holeField.classList.add('d-none'); // 체크박스 그룹 숨기기
            keywordField.classList.remove('d-none'); // 검색어 입력 필드 보이기
        }
    }
});
