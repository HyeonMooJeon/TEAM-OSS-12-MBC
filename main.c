#include<stdio.h>
#include<stdlib.h>

/*
2013601032 전현무
2013244103 정병철
*/

int main(){
 int first=0,last=0,result=0;
 char operator=NULL;
 
 printf("연산자를입력하세요");
 scanf("%c",&operator);

 printf("첫번째피연산자를입력하세요");
 scanf("%d",&first);
 
 printf("두번째피연산자를입력하세요");
 scanf("%d",&last);
 
 switch(operator){
 
   case+: result=(first+last);
   ;
   case-:result=(first-last);
   ;
   case*:result=(first*last);
   ;
   case/:result=(first/last);
   ;
  default:
   ;
  
 }
}
