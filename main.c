#define _CRT_SECURE_NO_WARNINGS
#include<stdio.h>
#include<stdlib.h>
/*
Project : Calculator
Member : 2013601032 전현무
         2013244103 정병철
         2013244128 김송봉
*/
void plus(int a, int c){
	int result = a + c;
	printf("%d + %d = %d",a,c,result);	
}
void subs(int s1, int s2) {
	int result = s1-s2;
	printf("%d - %d = %d", s1, s2, result);
}
void mult(int m1, int m2) {
	int result = m1 * m2;
	printf("%d * %d = %d", m1, m2, result);
}
void div(int num1, int num2) {
	double result = (double)num1 / num2;
	printf("%d / %d = %f, num1, num2, result);
}
 
void main() {
	int first = 0, last = 0;
	char a;
	printf("연산자를입력하세요");
	scanf("%c",&a);
	printf("첫번째피연산자를입력하세요");
	scanf("%d", &first);
	printf("두번째피연산자를입력하세요");
	scanf("%d", &last);
	switch (a) {
	ReEnter:;
	case'+': plus(first,last);
		break;
	case'-': subs(first,last);
		break;
	case'*': mult(first,last);
		break;
	case'/': div(first, last);
		break;

	default:
		printf("Insert err...");
		goto ReEnter;			
		break;
	}
}
