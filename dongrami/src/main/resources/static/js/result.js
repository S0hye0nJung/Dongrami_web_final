$(document).ready(function() {
    console.log('Document ready'); // 문서 준비 로그
    
    // URL에서 subcategoryId 추출
    const urlParams = new URLSearchParams(window.location.search);
    const subcategoryId = urlParams.get('subcategory_id'); // URL에서 subcategory_id 값을 가져옴

    // 사용자의 닉네임을 가져오기
    const userId = 1; // 실제 사용자 ID 데이터를 사용해야 합니다.
    const resultId = 1; // 실제 결과 ID 데이터를 사용해야 합니다.

    // 리뷰 쓰기 버튼 클릭 시 모달 열기
    $('#write-review').on('click', function() {
        console.log('Write review button clicked'); // 리뷰 쓰기 버튼 클릭 로그
        // 서버에서 bubble_slack_name 가져오기
        $.ajax({
            type: 'GET',
            url: `/allreview/subcategory/${subcategoryId}/bubbleSlackName`,
            success: function(response) {
                $('.user-role').text('#' + response); // bubble_slack_name 설정
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('에러 메시지:', textStatus, errorThrown);
                alert('정보를 가져오는 데 실패했습니다.');
            }
        });

        $('#review-modal').css('display', 'block');
        $('#rating .star').addClass('selected'); // 기본으로 모든 별을 채운 상태로 설정
        $('.rating-score').text('5점'); // 기본 점수를 5점으로 설정
    });

    // 모달 닫기 버튼 (X) 클릭 시 모달 닫기
    $('.close').on('click', function() {
        $('#review-modal').css('display', 'none');
        $('#message-modal').remove(); // 기존 메시지 모달 제거
    });

    // 모달 바깥 클릭 시 모달 닫기
    $(window).on('click', function(event) {
        if ($(event.target).is('#review-modal')) {
            $('#review-modal').css('display', 'none');
        }
        if ($(event.target).is('#message-modal')) {
            $('#message-modal').remove();
        }
    });

    // 별점 클릭 이벤트
    $('#rating .star').on('click', function() {
        const index = $(this).index() + 1;
        $('#rating .star').each(function(i) {
            if (i < index) {
                $(this).addClass('selected');
            } else {
                $(this).removeClass('selected');
            }
        });
        $('.rating-score').text(index + '점');
        $('#ratingScore').val(index); // 숨겨진 입력 필드에 점수 설정
    });

    // 리뷰 텍스트 글자 수 제한 및 카운터 업데이트
    $('#review-text').on('input', function() {
        const maxLength = 100;
        const currentLength = $(this).val().length;
        if (currentLength > maxLength) {
            $(this).val($(this).val().substring(0, maxLength));
        }
        $('#character-count').text(`${currentLength}/${maxLength}`);
    });

    function showModalMessage(header, message) {
        const modalHtml = `
            <div id="message-modal" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2>${header}</h2>
                    <p>${message}</p>
                </div>
            </div>
        `;
        $('body').append(modalHtml);
        $('#message-modal').css('display', 'block');

        // 모달 바깥 클릭 시 모달 닫기
        $(window).on('click', function(event) {
            if ($(event.target).is('#message-modal')) {
                $('#message-modal').remove();
            }
        });

        // 모달 닫기 버튼 클릭 시 모달 닫기
        $('#message-modal .close').on('click', function() {
            $('#message-modal').remove();
        });
    }

    // 리뷰 제출 버튼 클릭 시 처리 로직
    $('#submit-review').on('click', function(e) {
        e.preventDefault(); // 폼 제출 중지

        const reviewText = $('#review-text').val();
        const ratingScore = $('#ratingScore').val();

        if (!reviewText) {
            alert('리뷰 내용을 작성해주세요.');
            return false;
        }

        console.log('리뷰 텍스트:', reviewText);
        console.log('평점:', ratingScore);
        console.log('사용자 ID:', userId);
        console.log('소분류 ID:', subcategoryId);
        console.log('결과 ID:', resultId);

        // 폼 데이터를 서버로 전송
        $.ajax({
            type: 'POST',
            url: '/allreview/submit', // 수정된 URL
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({
                userId: userId, // 키 이름 수정
                subcategoryId: subcategoryId, // 키 이름 수정
                resultId: resultId, // 키 이름 수정
                rating: ratingScore,
                reviewText: reviewText // 키 이름 수정
            }),
            success: function(response) {
                console.log('서버 응답:', response);
                $('#review-modal').css('display', 'none'); // 모달 창 닫기
                showModalMessage('리뷰 작성이 완료되었습니다!', '마이페이지에서 확인하실 수 있습니다!');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('에러 메시지:', textStatus, errorThrown);
                alert('리뷰 제출에 실패했습니다. 다시 시도해주세요. \n' + textStatus + ': ' + errorThrown);
            }
        });
    });

    // 저장 버튼 클릭 시 처리 로직
    $('#save-result').on('click', function(e) {
        e.preventDefault(); // 기본 동작 중지

        console.log('Save button clicked'); // 버튼 클릭 로그

        const resultData = {
            resultId: 0, // 실제 결과 ID로 대체 (예: 0은 새 결과를 의미)
            userId: 1, // 실제 사용자 ID로 대체
            position1: '과거', // 실제 값으로 대체
            position2: '현재', // 실제 값으로 대체
            position3: '미래', // 실제 값으로 대체
            cardId1: 1, // 실제 카드 ID로 대체
            cardId2: 2, // 실제 카드 ID로 대체
            cardId3: 3, // 실제 카드 ID로 대체
            webReadingId: 1 // 실제 webReading ID로 대체
        };

        console.log('Sending data:', resultData); // 요청 데이터 로그

        $.ajax({
            type: 'POST',
            url: '/result/saveResult', // 결과 저장 URL
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(resultData),
            success: function(response) {
                console.log('서버 응답:', response);
                alert('타로 결과가 저장되었습니다! 마이페이지에서 확인하실 수 있습니다!');
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('에러 메시지:', textStatus, errorThrown);
                alert('결과 저장에 실패했습니다. 다시 시도해주세요.');
            }
        });
    });
});
