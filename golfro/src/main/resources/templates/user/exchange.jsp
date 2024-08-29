<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>포인트환전｜GOLFRO</title>
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet">
<link
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css"
    rel="stylesheet">
<link rel="stylesheet" href="../css/user_exchange.css" />
</head>
<body>
    <header th:replace="~{fragments/header :: header}"></header>

    <div class="container" id="user">
        <h3>내 포인트 환전하기</h3>
        <div class="card">
            <div class="card-header">
                <h5 class="card-title">내 포인트</h5>
            </div>

            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h5 class="display-4" th:text="${userPoint}">0</h5>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h5 class="card-title">나의 출금계좌</h5>
            </div>
            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h5 class="display-4">
                            <span
                                th:if="${#strings.startsWith(userAccount, 'KWANGJU')}"
                                th:text="'광주: ' + ${#strings.substring(userAccount, 8)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'BNK')}"
                                th:text="'경남: ' + ${#strings.substring(userAccount, 4)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'KB')}"
                                th:text="'국민: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'IBK')}"
                                th:text="'기업: ' + ${#strings.substring(userAccount, 4)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'NH')}"
                                th:text="'농협: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'DGB')}"
                                th:text="'대구: ' + ${#strings.substring(userAccount, 4)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'BUSAN')}"
                                th:text="'부산: ' + ${#strings.substring(userAccount, 5)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'KDB')}"
                                th:text="'산업: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'MG')}"
                                th:text="'새마을금고: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'SH')}"
                                th:text="'수협: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'SOL')}"
                                th:text="'신한: ' + ${#strings.substring(userAccount, 4)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'ShinHyup')}"
                                th:text="'신협: ' + ${#strings.substring(userAccount, 8)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'WOORI')}"
                                th:text="'우리: ' + ${#strings.substring(userAccount, 5)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'PO')}"
                                th:text="'우체국: ' + ${#strings.substring(userAccount, 2)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'JB')}"
                                th:text="'전북: ' + ${#strings.substring(userAccount, 2)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'kakao')}"
                                th:text="'카카오뱅크: ' + ${#strings.substring(userAccount, 5)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'Toss')}"
                                th:text="'토스: ' + ${#strings.substring(userAccount, 4)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'KEB')}"
                                th:text="'하나: ' + ${#strings.substring(userAccount, 3)}"></span>
                            <span
                                th:if="${#strings.startsWith(userAccount, 'SC')}"
                                th:text="'SC제일: ' + ${#strings.substring(userAccount, 2)}"></span>
                            <span
                                th:unless="${#strings.startsWith(userAccount, 'KWANGJU') or #strings.startsWith(userAccount, 'BNK') or #strings.startsWith(userAccount, 'KB') or #strings.startsWith(userAccount, 'IBK') or #strings.startsWith(userAccount, 'NH') or #strings.startsWith(userAccount, 'DGB') or #strings.startsWith(userAccount, 'BUSAN') or #strings.startsWith(userAccount, 'KDB') or #strings.startsWith(userAccount, 'MG') or #strings.startsWith(userAccount, 'SH') or #strings.startsWith(userAccount, 'SOL') or #strings.startsWith(userAccount, 'ShinHyup') or #strings.startsWith(userAccount, 'WOORI') or #strings.startsWith(userAccount, 'PO') or #strings.startsWith(userAccount, 'JB') or #strings.startsWith(userAccount, 'kakao') or #strings.startsWith(userAccount, 'Toss') or #strings.startsWith(userAccount, 'KEB') or #strings.startsWith(userAccount, 'SC')} th:text="
                                'unknownbank: ' + ${userAccount.split('/')[1]}"></span>
                        </h5>
                    </div>
                    <div class="col-md-6 text-end">
                        <a
                            th:href="@{/user/privacy(userid=${signedInUser}, account='change')}"
                            class="text-black">
                            <button class="exchangebtn">
                                <i class="fas fa-exchange-alt me-2"
                                    id="btn2"></i>계좌 변경
                            </button>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <h5 class="card-title">출금 가능금액</h5>
            </div>
            <div class="card-body">
                <div class="row align-items-center">
                    <div class="col-md-6">
                        <h5 class="display-4"
                            th:text="${userPoint - userWithdraw}">0</h5>
                    </div>
                    <div class="col-md-6">
                        <label for="withdraw" class="form-label">출금
                            신청금액</label> <input type="text" id="withdraw"
                            name="withdraw" class="form-control"
                            required placeholder="금액 입력"> <small
                            id="amountError"
                            class="form-text text-danger d-none">숫자만
                            입력 가능합니다.</small>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-body" id="last">
                <div class="row align-items-center">
                    <div class="col-md-6 mt-2 mb-1">
                        <label for="exchangePw" class="form-label"
                            id="exchangePw1">비밀번호</label> <input
                            type="password" id="exchangePw"
                            class="form-control" name="password"
                            required placeholder="비밀번호 입력">
                    </div>
                    <div class="col-md-6 text-end mt-3">
                        <button class="exchangebtn" id="btnExchange">
                            <i class="fas fa-coins me-2"></i>출금 신청
                        </button>
                    </div>
                    <div id="exchangeError"
                        class="alert alert-danger mt-3"
                        style="display: none;">
                        <p id="errorMessage"></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
					document.addEventListener('DOMContentLoaded', function() {
						// Your JavaScript code here
					});
				</script>

    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script