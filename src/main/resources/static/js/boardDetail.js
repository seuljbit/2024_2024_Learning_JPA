console.log("board detail page in!");

// 삭제 버튼
document.getElementById('delBtn').addEventListener('click', ()=> {
    document.getElementById('delForm').submit();
})

document.getElementById('listBtn').addEventListener('click', () => {
    location.href = "/board/list";
})

// 수정 버튼
document.getElementById('modBtn').addEventListener('click', () => {
    document.getElementById('title').readOnly = false;
    document.getElementById('content').readOnly = false;

    // 버튼 생성
    let modBtn = document.createElement("button");
    modBtn.setAttribute("type", "submit");
    modBtn.classList.add("btn", "btn-outline-warning");
    modBtn.innerText = "수정 완료";

    // 추가
    document.getElementById("modForm").appendChild(modBtn);
    document.getElementById("modBtn").remove();
    document.getElementById("delBtn").remove();
})