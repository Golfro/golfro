document.addEventListener('DOMContentLoaded', function() {s
    const findPwModal = document.getElementById('findPwModal');
    const findPwForm = document.getElementById('findPwForm');
    const findPwError = document.getElementById('findPwError');
    const errorMessagePw = document.getElementById('errorMessagePw');
    const setNewPwForm = document.getElementById('setNewPwForm');

    function resetModal() {
        findPwForm.reset();
        findPwError.style.display = 'none';
        errorMessagePw.textContent = '';
    }

    findPwModal.addEventListener('hidden.bs.modal', function() {
        resetModal();
        setNewPwForm.style.display = 'none';
    });

    document.getElementById('findIdForm').addEventListener('submit', function(e) {
        e.preventDefault();


        const name = document.getElementById('findIdName').value;
        const email = document.getElementById('findIdEmail').value;

        const findIdUrl = './findId';
        
        const data = { name, email };

        axios.post(findIdUrl, data)
            .then(function(response) {
                // 성공적으로 아이디를 찾은 경우
                document.getElementById('findIdResult').style.display = 'block';
                document.getElementById('foundId').textContent = `찾은 아이디: ${response.data.userid}`;
                document.getElementById('findIdError').style.display = 'none';
            })
            .catch(function(error) {
                if (error.response) {
                    // 서버가 에러 응답을 보낸 경우 (예: 404, 500 등)
                    document.getElementById('findIdError').style.display = 'block';
                    document.getElementById('errorMessage').textContent = error.response.data.error || '아이디를 찾을 수 없습니다.';
                    document.getElementById('findIdResult').style.display = 'none';
                } else {
                    // 요청이 실패한 경우 (네트워크 오류 등)
                    document.getElementById('findIdError').style.display = 'block';
                    document.getElementById('errorMessage').textContent = '서버와의 통신 중 오류가 발생했습니다.';
                    document.getElementById('findIdResult').style.display = 'none';
                }
            });
    });

    document.getElementById('findPwForm').addEventListener('submit', function(e) {
        e.preventDefault();
        var phone0 = document.querySelector('select[name="phone0"]').value;
        var phone1 = document.getElementById('phone1').value;
        var phone2 = document.getElementById('phone2').value;
        var phone3 = document.getElementById('phone3').value;

        var phone = phone0 + '/' + phone1 + '-' + phone2 + '-' + phone3;

        const userid = document.getElementById('findPwId').value;
        const email = document.getElementById('findPwEmail').value;
        const findPwUrl = `./findPw`;
        const data = { userid, email, phone }

        axios.post(findPwUrl, data)
            .then((response) => {
                if (response.data === 'Y') {
                    document.getElementById('setNewPwForm').style.display = 'block';
                    findPwError.style.display = 'none';
                    document.getElementById('findPwBtn').disabled = true;

                } else {
                    document.getElementById('errorMessagePw').textContent = '해당 정보와 일치하는 계정이 없습니다.';
                    document.getElementById('findPwError').style.display = 'block';
                }
            })
            .catch((error) => {
                console.log(error);
                document.getElementById('errorMessagePw').textContent = '오류가 발생했습니다. 다시 시도해주세요.';
                document.getElementById('findPwError').style.display = 'block';
            });

    });

    const setNewPwBtn = document.querySelector('button#setNewPwBtn');

    setNewPwBtn.addEventListener('click', function(e) {
        e.preventDefault();
        var phone0 = document.querySelector('select[name="phone0"]').value;
        var phone1 = document.getElementById('phone1').value;
        var phone2 = document.getElementById('phone2').value;
        var phone3 = document.getElementById('phone3').value;

        var phone = phone0 + '/' + phone1 + '-' + phone2 + '-' + phone3;

        const password = document.getElementById('userPassword').value;
        const userid = document.getElementById('findPwId').value;
        const email = document.getElementById('findPwEmail').value;
        const data = { userid, email, phone, password }
        const updatePwUrl = `./updatePw`;
        axios.post(updatePwUrl, data)
            .then((response) => {

                if (response.data === 'Y') {
                    alert('비밀번호가 성공적으로 변경 되었습니다.');
                    window.location.href = '/gaebokchi/user/signin';
                } else {
                    alert('비밀번호를 다시 확인하세요.');
                }

            })
            .catch((error) => console.log(error));

    });
    const password = document.getElementById('userPassword');
    const passwordConfirm = document.getElementById('userPasswordConfirm');
    const passwordStrength = document.getElementById('passwordStrength');
    const passwordMatch = document.getElementById('passwordMatch');
    let reg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
    function checkPasswordMatch() {
        if (password.value === '' || passwordConfirm.value === '' || password.value !== passwordConfirm.value) {
            setNewPwBtn.disabled = true; // 비밀번호가 비어있거나 비밀번호와 비밀번호 확인이 일치하지 않으면 버튼 비활성화
        } else {
            setNewPwBtn.disabled = false; // 비밀번호가 모두 입력되고 일치하면 버튼 활성화
        }
    }

    // 페이지 로드 시 한 번 호출하여 초기 상태 설정
    checkPasswordMatch();

    // 비밀번호 입력과 확인 입력이 변경될 때마다 비교하여 버튼 상태 업데이트
    password.addEventListener('input', checkPasswordMatch);
    passwordConfirm.addEventListener('input', checkPasswordMatch);

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

    if (password && passwordConfirm && passwordStrength && passwordMatch) {
        password.addEventListener('input', function() {
            if (this.value.length > 0) {
                const strength = checkPasswordStrength(this.value);
                const strengthText = getStrengthText(strength);
                const strengthColor = getStrengthColor(strength);

                passwordStrength.textContent = `비밀번호 안정성: ${strengthText}`;
                passwordStrength.style.color = strengthColor;
                if (reg.test(this.value)) {
                    passwordStrength.textContent += ' (유효한 비밀번호)';
                } else {
                    passwordStrength.textContent += ' (유효하지 않은 비밀번호)';
                    updateSubmitButton();
                }
            } else {
                passwordStrength.textContent = '';
            }
        });

        passwordConfirm.addEventListener('input', function() {
            if (this.value === password.value) {
                passwordMatch.textContent = '비밀번호가 일치합니다.';
                passwordMatch.style.color = 'green';
            } else {
                passwordMatch.textContent = '비밀번호가 일치하지 않습니다.';
                passwordMatch.style.color = 'red';
            }
        });
    } else {
        console.error('비밀번호 관련 요소를 찾을 수 없습니다.');
    }
});