#include <stdio.h>
#include <inttypes.h>
int main(int argc, char **argv){
int64_t r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
int64_t stack[100];
int64_t *sp = &stack[99];
int64_t *fp = &stack[99];
int64_t *ra = &&exit;
goto mainEntry;
int64_t reserved = 0, x = 0, y = 0;
mainEntry:
sp = sp - 2;
*(sp+2) = fp;
*(sp+1) = ra;
fp = sp;
sp = sp - 6;
r1 = 0;
*(fp-2) = r1;
reserved = *(fp-2);
r1 = 2;
*(fp-2) = r1;
r1 = 3;
*(fp-3) = r1;
r1 = *(fp-2);
r2 = *(fp-3);
r3 = r1 + r2;
*(fp-2) = r3;
x = *(fp-4);
r1 = 1;
*(fp-2) = r1;
r1 = 2;
*(fp-3) = r1;
r1 = *(fp-2);
r2 = *(fp-3);
if(r1 < r2) goto truelabel1;
goto falselabel0;
truelabel1:
r1 = 4;
*(fp-4) = r1;
r1 = 3;
*(fp-5) = r1;
r1 = *(fp-4);
r2 = *(fp-5);
if(r1 > r2) goto truelabel0;
goto falselabel0;
truelabel0:
r1 = 1;
*(fp-2) = r1;
r1 = *(fp-6);
r2 = *(fp-2);
r3 = r1 + r2;
*(fp-6) = r3;
reserved = *(fp-3);
falselabel0:
sp = sp + 6;
fp = *(sp+2);
ra = *(sp+1);
sp = sp + 2;
goto *ra;
exit:
return reserved;
}