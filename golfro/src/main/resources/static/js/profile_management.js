/**
 * /user/profile.html에 포함.
 */

document.addEventListener('DOMContentLoaded', () => {
	const userid = document.querySelector('input#userid').value;
	
	const grade = document.querySelector('input#grade').value;
	
	const titleNickname = document.querySelector('h5#titleNickname');
	
	const btnEditNickname = document.querySelector('button#btnNicknameUpdate');
	btnEditNickname.addEventListener('click', viewEditNickname);
	
	const editNickname = document.querySelector('div#btnNicknameGroup');
	
	const btnCancelNickname = document.querySelector('button#btnCancelNickname');
	btnCancelNickname.addEventListener('click', cancelNicknameEvent);
	
	const btnEditIntroduce = document.querySelector('button#btnIntroduceMyself');
	if (btnEditIntroduce !== null) {
	   btnEditIntroduce.addEventListener('click', viewEditIntroduce);
	}
	
	const editIntroduce = document.querySelector('div#btnIntroduceGroup');
	
	const btnCancelIntroduce = document.querySelector('button#btnCancelIntroduce');
	if (btnCancelIntroduce !== null) {
	   btnCancelIntroduce.addEventListener('click', cancelIntroduceEvent);
	}
	
	let userNickname = document.querySelector('input#nickname').value;
	const nickname = document.querySelector('input#nickname');
	
	let userIntroduce = document.querySelector('input#introduce');
	const introduce = document.querySelector('input#introduce');
	
	const btnSaveNickname = document.querySelector('button#btnSaveNickname');
	btnSaveNickname.addEventListener('click', updateNickname);
	
	const btnSaveIntroduce = document.querySelector('button#btnSaveIntroduce');
	if (btnSaveIntroduce !== null) {
	   btnSaveIntroduce.addEventListener('click', updateIntroduce);
	}
	
	const btnFileUpload = document.querySelector('input#input_file');
	btnFileUpload.addEventListener('change', fileUpload);
	
	const btnFileRemove = document.querySelector('a#fileRemove');
	btnFileRemove.addEventListener('click', fileRemove);
	
	function viewEditNickname() {
		editNickname.classList.remove('d-none');
		nickname.readOnly = false;
	}
	
	function viewEditIntroduce() {
		editIntroduce.classList.remove('d-none');
		introduce.readOnly = false;
	}
	
	function cancelNicknameEvent() {
		editNickname.classList.add('d-none');
		nickname.value = userNickname;
		nickname.readOnly = true;
	}
	
	function cancelIntroduceEvent() {
		editIntroduce.classList.add('d-none');
		introduce.value = userIntroduce.value;
		introduce.readOnly = true;
	}
	
	function updateNickname() {
		const uri = `/user/checkname?nickname=${nickname.value}`;

		axios.get(uri)
			.then((response) => {
				if (response.data === 'Y') {
					let result = confirm('사용할 수 있는 닉네임입니다.\n저장하시겠습니까?');
					if (result) {
						saveNickname();
					}
				} else {
					alert('사용할 수 없는 닉네임입니다.');
				}
			})
			.catch((error) => {
				console.log(error);
			});
	}
	
	function updateIntroduce() {
		const uri = `/user/professional/${userid}`;
		axios.put(uri, { career: introduce.value })
			.then((response) => {
				console.log(response.data);
				introduce.value = response.data.career;
				userIntroduce.value = response.data.career;
				
				editIntroduce.classList.add('d-none');
				introduce.readOnly = true;
			})
			.catch((error) => {
				console.log(error);
			});
	}
	
	function saveNickname() {
		const uri = `/user/updateNickname`;
		axios.put(uri, { nickname: nickname.value, grade: grade })
			.then((response) => {
                console.log(response.data);
                titleNickname.textContent = response.data.nickname;
                nickname.value = response.data.nickname;
				userNickname = response.data.nickname;
				
                editNickname.classList.add('d-none');
                nickname.readOnly = true;
			})
			.catch((error) => {
				console.log(error);
			});
	}
	
	function fileUpload() {
	    var fileInput = document.querySelector('input#input_file');
	    var file = fileInput.files[0];

	    if (!file) {
	        console.log('파일을 선택해주세요.');
	        return;
	    }

	    // 파일 업로드 진행 상황을 사용자에게 알리기 위한 UI 로직 (예: 로딩 스피너, 진행바 등)

	    var formData = new FormData();
	    formData.append('file', file);

	    const uri = '/user/file/image';

	    axios.post(uri, formData, {
	        headers: {
	            'Content-Type': 'multipart/form-data'
	        },
	        onUploadProgress: function(progressEvent) {
	            // 업로드 진행 상황을 사용자에게 알림 (예: 진행바 업데이트)
	            var percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
	            console.log('Upload Progress: ' + percentCompleted + '%');
	        }
	    })
	    .then((response) => {
	        // 업로드 성공 시 처리
	        console.log('File uploaded successfully');
	        location.reload(true);  // 페이지 리로드 또는 다른 처리
	    })
	    .catch((error) => {
	        // 업로드 실패 시 처리
	        console.error('Error uploading file:', error);
	        // 실패 메시지를 사용자에게 보여주는 등의 처리
	    });
	}
	
	function fileRemove() {
		const uri = `/user/file/remove?userid=${userid}`;

		axios.get(uri)
			.then((response) => {
				location.reload(true);
			})
			.catch((error) => {
				console.log(error);
			});
	}
});