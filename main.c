#define _CRT_SECURE_NO_WARNINGS

#include<stdio.h>
#include<stdlib.h>

/*
Project : Calculator
Member : 2013601032 전현무
         2013244103 정병철
         201xxxxxxx 김송봉
*/
int queue() {
  
}
void plus(int a, int c){
	
	char result = a + c;
	
	printf("%d + %d = %d",a,c,result);	
}

int main() {
	int first = 0, last = 0, result = 0;
	char a;

	printf("연산자를입력하세요");
	scanf("%c",&a);

	printf("첫번째피연산자를입력하세요");
	scanf("%d", &first);

	printf("두번째피연산자를입력하세요");
	scanf("%d", &last);

	switch (a) {

	case'+': plus(first,last);
		break;
	case'-':
		break;
	case'*':
		break;
	case'/':
		break;

	default:
		break;
	}
	
}
