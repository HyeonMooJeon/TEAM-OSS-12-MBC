#define _CRT_SECURE_NO_WARNINGS

#include<stdio.h>
#include<stdlib.h>

/*
2013601032 전현무
2013244103 정병철
*/

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

	case'+': result = (first + last);
		break;
	case'-':result = (first - last);
		break;
	case'*':result = (first*last);
		break;
	case'/':result = (first / last);
		break;

	default:
		break;

	}
}
