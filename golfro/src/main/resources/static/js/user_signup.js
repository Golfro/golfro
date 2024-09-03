document.addEventListener('DOMContentLoaded', () => {
    const idbutton = document.querySelector('button#idbutton');
    const nicknamebutton = document.querySelector('button#nicknamebutton');
    const submitButton = document.querySelector('button#submitButton');
    let useridChecked = false;
    let nicknameChecked = false;


    const emailPrefix = document.getElementById('emailPrefix');
    const emailSeparator = document.getElementById('emailSeparator');
    const emailDiv = document.getElementById('emailDiv');

    const phone0 = document.getElementById('phone0');
    const phone1 = document.getElementById('phone1');
    const phone2 = document.getElementById('phone2');
    const phone3 = document.getElementById('phone3');
    const phoneDiv = document.getElementById('phoneDiv');

    const postCode = document.getElementById('postCode');
    const addressMain = document.getElementById('addressMain');
    const addressDetail = document.getElementById('addressDetail');
    const addressDiv = document.getElementById('addressDiv');

    const acceptLicense = document.getElementById('accept');
    const acceptDiv = document.getElementById('acceptDiv');

    addressDetail.addEventListener('blur', checkAddress)

    function checkAddress() {
        if (postCode.value === '' || addressMain.value === '') {
            submitButton.disabled = true;
            addressDiv.textContent = '주소를 검색 후 입력해 주세요.';
        } else {
            submitButton.disabled = false;
            addressDiv.textContent = '';
        }
    }

    function checkAccept() {
        if (acceptLicense.value === '') {
            acceptDiv.textContent = '';
            return;
        }

        const accept = acceptLicense.value;
        const acceptUri = `./checkAccept?accept=${accept}`;

        axios.get(acceptUri)
            .then(function(response) {
                if (response.data === 'N') {
                    acceptDiv.textContent = '이미 가입된 라이센스 번호입니다.';
                    acceptDiv.style.color = 'red';
                    submitButton.disabled = true;
                } else {
                    acceptDiv.textContent = '사용 가능한 라이센스 번호입니다.';
                    acceptDiv.style.color = 'green';
                    submitButton.disabled = false;
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                acceptDiv.textContent = '라이센스 번호 확인 중 오류가 발생했습니다.';
                acceptDiv.style.color = 'red';
            });
    }

    acceptLicense.addEventListener('blur', checkAccept);

    function checkPhone() {

        if (phone0.value === '' || phone1.value === '' || phone2.value === '' || phone3.value === '') {
            phoneDiv.textContent = '';
            return;
        }

        const phone = phone0.value + '/' + phone1.value + '-' + phone2.value + '-' + phone3.value;
        const phoneUri = `./checkPhone?phone=${phone}`;

        axios.get(phoneUri)
            .then(function(response) {
                if (response.data === 'N') {
                    phoneDiv.textContent = '이미 가입된 휴대폰 번호입니다.';
                    phoneDiv.style.color = 'red';
                    submitButton.disabled = true;
                } else {
                    phoneDiv.textContent = '사용 가능한 휴대폰 번호입니다.';
                    phoneDiv.style.color = 'green';
                    submitButton.disabled = false;
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                phoneDiv.textContent = '휴대폰 번호 확인 중 오류가 발생했습니다.';
                phoneDiv.style.color = 'red';
            });
    }


    phone3.addEventListener('blur', checkPhone);

    phone0.addEventListener('change', () => { phoneDiv.textContent = ''; });
    phone1.addEventListener('input', () => { phoneDiv.textContent = ''; });
    phone2.addEventListener('input', () => { phoneDiv.textContent = ''; });


    function checkEmail() {
        const email = emailPrefix.value + '@' + emailSeparator.value;
        const emailUri = `./checkEmail?email=${email}`;
        // 이메일이 비어있으면 체크하지 않음
        if (emailPrefix.value === '') {
            emailDiv.textContent = '';
            return;
        }

        axios.get(emailUri)
            .then(function(response) {
                if (response.data === 'N') {
                    emailDiv.textContent = '이미 가입된 사용자입니다.';
                    emailDiv.style.color = 'red';
                    submitButton.disabled = true;
                } else {
                    emailDiv.textContent = '사용 가능한 이메일입니다.';
                    emailDiv.style.color = 'green';
                    submitButton.disabled = false;
                }
            })
            .catch(function(error) {
                console.error('Error:', error);
                emailDiv.textContent = '이메일 확인 중 오류가 발생했습니다.';
                emailDiv.style.color = 'red';
            });
    }

    emailPrefix.addEventListener('blur', checkEmail);

    emailSeparator.addEventListener('blur', checkEmail);

    // Submit 버튼 비활성화 함수
    function updateSubmitButton() {
        submitButton.disabled = !(useridChecked && nicknameChecked);
    }

    // 초기 상태에서 submit 버튼 비활성화
    updateSubmitButton();

    idbutton.addEventListener('click', () => {
        const userid = document.querySelector('input#userid').value;

        // 유효성 검사를 수행합니다.
        const regex = /^[a-zA-Z0-9]{1,15}$/;
        if (!regex.test(userid)) {
            alert('아이디는 15자 이하의 영문 대소문자와 숫자로만 입력 가능합니다.');
            return;
        }

        const iduri = `./checkUserid?userid=${userid}`;
        axios
            .get(iduri)
            .then((response) => {
                if (response.data === 'Y') {
                    alert('사용 가능한 아이디 입니다.');
                    useridChecked = true;
                } else {
                    alert('사용 불가능한 아이디 입니다.');
                    useridChecked = false;
                }
                updateSubmitButton();
            })
            .catch((error) => {
                console.log('error', error);
                useridChecked = false;
                updateSubmitButton();
            });
    });


    nicknamebutton.addEventListener('click', () => {
        const nickname = document.querySelector('input#nickname').value;
        const nickuri = `./checkNickname?nickname=${nickname}`;
        axios
            .get(nickuri)
            .then((response) => {
                if (response.data === 'Y') {
                    alert('사용 가능한 닉네임 입니다.')
                    nicknameChecked = true;
                } else {
                    alert('사용 불가능한 닉네임 입니다.')
                    nicknameChecked = false;
                }
                updateSubmitButton();
            })
            .catch((error) => {
                console.log('error', error);
                nicknameChecked = false;
                updateSubmitButton();
            });
    });




    const password = document.getElementById('userPassword');
    const passwordConfirm = document.getElementById('userPasswordConfirm');
    const passwordStrength = document.getElementById('passwordStrength');
    const passwordMatch = document.getElementById('passwordMatch');
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