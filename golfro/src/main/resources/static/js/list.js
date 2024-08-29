/*
 * list.jsp 연결
 */


document.addEventListener('DOMContentLoaded', function () {
    toggleSearchField(); // 페이지 로드 시 필드를 초기화
});

function toggleSearchField() {
    var searchCategory = document.getElementById("searchCategory").value;
    var clubSelectField = document.getElementById("clubSelectField");
    var textSearchSelectField = document.getElementById("textSearchSelectField");
    var searchSelectionField = document.getElementById("searchSelectionField");

    if (searchCategory === "searchClubs") {
        clubSelectField.style.display = "block";
        textSearchSelectField.style.display = "none";
        searchSelectionField.style.display = "none";
    } else if (searchCategory === "searchSelection") {
        clubSelectField.style.display = "none";
        textSearchSelectField.style.display = "none";
        searchSelectionField.style.display = "block";
    } else {
        clubSelectField.style.display = "none";
        textSearchSelectField.style.display = "block";
        searchSelectionField.style.display = "none";
    }
}
