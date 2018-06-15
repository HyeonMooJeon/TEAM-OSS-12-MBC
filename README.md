# TEAM-OSS-12-MBC


Project : 아동 위치 확인 시스템
Team name : TEAM_MBC
Member : Jun Hyun Moo
         Kim Song Bong
         Jung Byeong Cheol

		 본 프로그램 특징{
			본 프로그램은 아동 위치 확인 시스템으로 소켓연결 기반 프로그램이며 java Server와 두개의 Android Client를 가지고 있다.
			자식의 핸드폰에 Child_Client를 다운받아 실행시킬경우 자식의 위치를 지속적으로 서버로 보내준다 (위도,경도)
			서버는 위도,경도를 받아서 부모가 다운받아 부모의 핸드폰인 Parents_Client에게 보내주는데이 위도,경도 데이터를 활용하여
			아동이 해당 지점을 지나가게 되면 날자,시간 등 정보를 안드로이드 GoogleMap상에 오버레이 시켜 마커 형식으로 출력시킨다. 마커형식은 발자국 모양임
			또한 이동경로를 버튼을 누를시 GoogleMap상에 출력시켜 어떻게 해당지점까지 이동하였는지를 출력시킨다.
			또한 범위를 지정하여 원을 그려준다.
			
		 }

		 사용 프로그램
			Android Studio (Client용)  {
				minSDKversion 22
				MaxSDKversion 27
			}
			Eclips (Server용)
			
		Guide{
			1.Github : https://github.com/HyeonMooJeon/TEAM-OSS-12-MBC
				-위의 주소에서 파일을 다운로드 받는다.
			2.각 풀더별로 Client와 Server가 나누어져 있으며 각각의 파일에 맡게 안드로이드 스튜디오를 생성하여 코드를 복사하여 붙여넣는다.
			3. (안드로이드 풀더를 생성하여 직접 RUN할경우만 진행 )Parents_Client의 Map을 출력하기 위해서는 Google Map API Key를 대입하는 부분은 Manifest.xml의30번째 라인의 API 키값을 다음과 같이 진행해야한다.
				-http://bitsoul.tistory.com/139 참고하여 API키값을 생성하여 30번째줄을 바꿔넣어주어야 한다.
				
			4. 위와같이 place 키도 변경을 해주어야 함 다음 URL의 "표준 Google Places API Web Service를 사용 중인 경우"를 참고하시오 https://developers.google.com/places/web-service/get-api-key?hl=ko 
				-place 키값은 MainActivity.java 파일의 999번째 Line에 존재한다
			}
			
			
		 
		 앱 사용법{
			일반인 사용법-
				전제조건 1 : 서버가 실행되고 있어야한다.(서비스를 할경우)
				1. Child Client를 자식 핸드폰에 다운로드 시켜 Tag값을 입력후 시작버튼을 누른다.
				2. Parents Client를 부모 핸드폰에 다운로드 시켜 아이 핸드폰의 Tag값과 똑같게 부모 핸드폰에 입력하여 위치찾기 시작 버튼을 누른다.
				3. 아이의 위치가 부모 핸드폰의 Map창에 나오는것을 볼수있다.
				
			전제조건 1 : 서버가 실행되고 있어야한다.
				-서버는 이클립스로 작성되어있음
				-서버의 PORT는 이클립스창에서 수정할수 있으며 변수 PORT를 변경하면 가능하다.
			
			Child_Client :
				
				1. Child Client App을 실행시키고 Start 버튼을 누르기 전에 TAG칸에 부모만 입력할수 있는 태그를 입력한다.
				2. Child Client에서 위치 정보를 부모에게 전송시키기 위해 서버에게 송신하는 작업 버튼인 "시작" 버튼을 클릭한다.
				3. 현재 위도, 경도가 출력이 되면서 신호등처럼 진행중인 이펙트가 출력되면 전송이 된다.
				
			Parents_Client :
		
				위치 찾기 시작 버튼{
					1. Parents_Client App을 실행시키고 TAG칸에 Child Client 에서 입력한 TAG를 입력하고 "위치 찾기 시작" 버튼을 클릭한다.
					2. 정상적으로 Server가 동작한다면 Child Client에서 서버에 전송한 위도, 경도 정보가 수신되며 자동으로 현재 위치를 갱신한다.
						-Childe Client에서는 10초간격으로 Data를 전송한다.
						-Child Client에서는 4초간격으로 맵위치를 갱신하며 2초간격으로 업데이트를 요청한다.
				}
				경찰서 찾기 버튼{
					1. Google Map API Place Information 의 Data를 가져와 각각 정의된 (ex)음식점, 경찰서, 학교 )정보에 대한 위도, 경도값을 받아서 출력한다.			
					2. 현재 출력되고 있는 Child의 위치를 기점으로 인근 3KM내의 모든 경찰서 정보를 Map창에 출력시킨다.
				}	
				이동경로 추적 버튼{
					1-1. 이동경로에 대한 정보는 on/off식 버튼으로 제공하며 on이 될 경우 현재까지 저장된 아이의 위도, 경도 를 바탕으로 사용자의 Map에 출력시킨다.
					1-2. 출력된 정보는 저장된 정보가 두개이상일때(20초)부터 사용할수 있다.
				}
					FootPrint 기능{
					2-1. 이동경로 추적 버튼에서 동시에 제공하는 FootPrint기능은 10분 간격으로 아이가 지나간 이동경로 선 위에 마커가 생성되며 발자국 모양으로 제공된다.
					2-2. 제공된 발자국 모양의 마커는 클릭시 아이가 해당지점에 지나갔을 때의 Date 정보(시간,날자)를 사용자에게 제공한다.
				}
				위치 기점 View창 이동{
				1. On/Off 버튼형식으로 제공이 되며 On이 될경우 지속적으로 카메라의 위치를 아이의 위도, 경도 값으로 카메라를 갱신한다.
					-off이 될 경우 지속적으로 카메라위치 갱신을 취소한다.
				}
		 
		 }