/**
 * /user/modify.html에 포함.
 */

// HTML DOM(Document Obejct Model) 컨텐트 로딩이 끝났을 때, 이벤트 리스너를 실행.
document.addEventListener('DOMContentLoaded', () => {
	let passwordChecked = true; // 비밀번호 필드 작성 여부 체크.

	// form 요소를 찾음:
	const modifyForm = document.querySelector('form#modifyForm');

	// 저장 버튼 찾음:
	const btnUpdate = document.querySelector('button#btnUpdate');

	const password = document.querySelector('input#password');

	const changePassword = document.querySelector('input#changePassword');
	changePassword.addEventListener('input', checkPassword);

	const btnAddressSearch = document.querySelector('input#btnAddressSearch');
	btnAddressSearch.addEventListener('click', searchAddress);

	function searchAddress() {
		new daum.Postcode({
			oncomplete: function(data) {
				// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

				// 각 주소의 노출 규칙에 따라 주소를 조합한다.
				// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
				var addr = ''; // 주소 변수

				// 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
				if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
					addr = data.roadAddress;
				} else { // 사용자가 지번 주소를 선택했을 경우(J)
					addr = data.jibunAddress;
				}

				// 우편번호와 주소 정보를 해당 필드에 넣는다.
				document.querySelector('input#postcode').value = data.zonecode;
				document.querySelector("input#mainAddress").value = addr;
				// 커서를 상세주소 필드로 이동한다.
				document.querySelector("input#detailAddress").focus();
			}
		}).open();
	}
	
	const passwordStrength = document.getElementById('passwordStrength');
	let reg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
	
	function checkPasswordStrength(password) {
		let strength = 0;
	 	if (password.match(/[a-z]+/)) strength += 1;
	  	if (password.match(/[A-Z]+/)) strength += 1;
	   	if (password.match(/[0-9]+/)) strength += 1;
	 	if (password.match(/[@$!%*#?&]+/)) strength += 1;

		return strength;
	}

	function getStrengthText(strength) {
		switch (strength) {
	  		case 0: return "매우 약함";
	    	case 1: return "약함";
	    	case 2: return "보통";
	       	case 3: return "강함";
	    	case 4: return "매우 강함";
	 	}
	}

	function getStrengthColor(strength) {
		switch (strength) {
	    	case 0: return "red";
	   		case 1: return "orange";
	      	case 2: return "blue";
	      	case 3: return "lightgreen";
	      	case 4: return "green";
	  	}
	}
	
	function checkPassword(event) {
		if (event.target.value === '' || changePassword.value === password.value) { // inputPassword.value
			passwordChecked = false;
		} else {
			if (this.value.length > 0) {
				const strength = checkPasswordStrength(this.value);
			  	const strengthText = getStrengthText(strength);
				const strengthColor = getStrengthColor(strength);

				passwordStrength.textContent = `비밀번호 안정성: ${strengthText}`;
			 	passwordStrength.style.color = strengthColor;
			  	if (reg.test(this.value)) {
			 		passwordStrength.textContent += ' (유효한 비밀번호)';
					passwordChecked = true;
			 	} else {
			    	passwordStrength.textContent += ' (유효하지 않은 비밀번호)';
					passwordChecked = false;
				}
			}
		}
	}

	// 저장 버튼에 클릭 이벤트 리스너를 설정.
	btnUpdate.addEventListener('click', () => {
		// 제목과 내용이 비어있는 지 체크:
		/* const title = inputTitle.value; // input 요소에 입력된 값.
		const content = textareaContent.value; // textarea 요소에 입력된 값.
		if (title === '' || content === '') {
			alert('제목과 내용은 반드시 입력해야 합니다!');
			return; // 함수 종료
		} */

		if (!passwordChecked) {
			alert('비밀번호를 확인하세요.');
			return;
		}

		const result = confirm('변경된 내용을 저장할까요?');
		if (passwordChecked && result) {
			modifyForm.method = 'post'; // 폼 제출 방식 설정.
			modifyForm.action = 'update'; // 폼 제출 요청 주소 설정.
			modifyForm.submit(); // 폼 제출(서버로 요청을 보냄).
		}
	});
});