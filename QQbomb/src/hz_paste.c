#include <stdio.h>
#include <Windows.h>

int main(int argc, char *argv[]) {
	if(argc!=2){
		printf("�������Ϸ���\nhz_paste�÷�:hz_paste + <�ո�> + <���ڶ���>");
		return 1;
	}
	HWND w = FindWindow(0,argv[1]); 
	if(w==NULL){
		printf("���ڲ�����!");
		return 1;
	}
	SendMessage(w,WM_PASTE,0, 0);
	SendMessage(w,WM_KEYDOWN,VK_RETURN,0);
	printf("over.");
	return 0;
}
