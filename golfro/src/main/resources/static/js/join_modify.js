document.addEventListener('DOMContentLoaded', function() {
	const modifyForm = document.querySelector('form#modifyForm');
	const btnUpdate = document.querySelector('button#btnUpdate');
	// 제목, 그린피, 홀수, 내용 입력란
	const title = document.querySelector('input[name="title"]');
	const greenfee = document.querySelector('input[name="greenfee"]');
	const hole = document.querySelector('input[name="hole"]');
	const content = document.querySelector('textarea[name="content"]');

	document.getElementById('btnUpdate').addEventListener('click', function() {
		const result = confirm('변경 내용을 저장할까요?');
		if (result) {
			const modifyForm = document.getElementById('modifyForm'); // 폼 ID 수정된 부분
			modifyForm.action = 'update'; // 요청 주소
			modifyForm.method = 'POST'; // 요청 방식
			modifyForm.submit(); // 폼 양식 데이터 제출(서버로 요청 보냄).
		}
	});

	function execDaumPostcode() {
		new daum.Postcode({
			oncomplete: function(data) {
				var addr = data.address;
				document.getElementById('address').value = addr;
			}
		}).open();
	}


	// 제목, 그린피, 홀수 입력란 길이 체크 이벤트 리스너
	title.addEventListener('input', function() {
		if (title.value.trim().length > 20) {
			alert('골프장명은 20자 이내로 입력해주세요!');
			title.value = title.value.trim().substring(0, 20);
		}
	});

	greenfee.addEventListener('input', function() {
		if (greenfee.value.trim().length > 6) {
			alert('그린피는 10만원 단위 이내로 입력해주세요!');
			greenfee.value = greenfee.value.trim().substring(0, 6);
		}
	});

	hole.addEventListener('input', function() {
		if (hole.value.trim().length > 2) {
			alert('홀은 2자리로 입력해주세요!');
			hole.value = hole.value.trim().substring(0, 2);
		}
	});

	content.addEventListener('input', function() {
		if (content.value.trim().length > 1000) {
			alert('내용은 1000자 이내로 입력해주세요!');
			content.value = content.value.trim().substring(0, 1000);
		}
	});

});


