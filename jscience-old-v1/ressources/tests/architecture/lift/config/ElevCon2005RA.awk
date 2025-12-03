BEGIN		{
	exit;
}

//		{
	;
}

END		{
	for (i=0;i<720;i++) {
		NC=int((i/240)+2);
		CC=(int((i%240)/80)+2)*3;
		KT=int(i/20)%4;
		PG=(i%20)+1;
	
		KTA[0]="NORMAL";
		KTA[1]="DOOR";
		KTA[2]="FAST";
		KTA[3]="DOOR+FAST";
	
		print("\""NC"\",\""CC"\",\""KTA[KT]"\",\""PG"\"");
	}
}
