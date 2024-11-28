console.log("boardDetail.js in");
document.getElementById('listBtn').addEventListener('click',()=>{
    //리스트로 이동
    location.href="/board/list";
});
document.getElementById('delBtn').addEventListener('click',()=>{
    //delForm 이 submit으로 전송
    document.getElementById('delForm').submit();
})

document.getElementById('modBtn').addEventListener('click',()=>{
// title, content 의 readonly를 해지   readOnly = true  / false
    document.getElementById('title').readOnly = false;
    document.getElementById('content').readOnly = false;
    document.getElementById('password').style.visibility = 'visible'

    // modBtn delBtn 삭제
    document.getElementById('modBtn').remove();
    document.getElementById('delBtn').remove();

    // modBtn => submit 버튼으로 변경 추가
    // <button></button>
    let modBtn = document.createElement('button');

    // <button type="submit"></button>
    modBtn.setAttribute('type','submit');
    modBtn.setAttribute('id', 'regBtn');

    //class="btn btn-outline-warning"
    modBtn.classList.add('btn','btn-outline-warning');

    // <button type="submit" class="btn btn-outline-warning">submit</button>
    modBtn.innerText="submit";

    // form 태그의 자식 요소로 추가 - form 에 가장 마지막에 추가
    document.getElementById('modForm').appendChild(modBtn);

    // file-x 버튼 disabled 해지
    document.getElementById('trigger').disabled = false;

    let fileDelBtn = document.querySelectorAll(".file-x");
    for (let delBtn of fileDelBtn) {
        delBtn.style.visibility = 'visible' ; // 숨김 해제
        delBtn.disabled = false; // 버튼 활성화
    }




});

document.addEventListener('click', (e) => {
    if(e.target.classList.contains('file-x')) {
        console.log(e.target);
        let uuid = e.target.dataset.uuid;
        removeFileFromServer(uuid).then(result => {
            if(result > 0) {
                e.target.closest('li').remove();
                alert("파일을 삭제하였습니다.")
            }
        });
    }
});

// 비동기 데이터 보내기
async function removeFileFromServer(uuid) {
    try {
        const url = '/board/file/' + uuid;
        const config = { method: 'delete' };
        const resp = await fetch(url, config);
        const result = await resp.text();

        return result;
    } catch (error) {
        console.log(">>> error!! : " + error);
    }
}