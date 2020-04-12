#include <stdio.h>
#include <Windows.h>

int main(int argc, char *argv[]) {
	if(argc!=2){
		printf("参数不合法。\nhz_paste用法:hz_paste + <空格> + <窗口对象>");
		return 1;
	}
	HWND w = FindWindow(0,argv[1]); 
	if(w==NULL){
		printf("窗口不存在!");
		return 1;
	}
	SendMessage(w,WM_PASTE,0, 0);
	SendMessage(w,WM_KEYDOWN,VK_RETURN,0);
	printf("over.");
	return 0;
}
