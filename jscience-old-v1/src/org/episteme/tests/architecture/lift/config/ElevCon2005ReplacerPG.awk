BEGIN		{
	while(getline < "PGSkeleton.cs") {
		Buffer=Buffer""$0"\n";
	}
	exit;
}

//		{
	;
}

END		{
	for (i=1; i<21; i++) {
		Buffer2=Buffer;
		gsub("_NAME_","Poisson Passenger Generator Example ("i"/20)",Buffer2);
		gsub("_RS_","123456789",Buffer2);
		if (i<11) {
			for (j=6;j>0;j--) {
				gsub("_F"j"G_","0.0",Buffer2);
			}
			gsub("_F0G_",0.0024*i,Buffer2);
			gsub("_F-1G_",0.0004*i,Buffer2);
		} else {
			for (j=6;j>-2;j--) {
				gsub("_F"j"G_",0.0004*(i-10),Buffer2);
			}
		}
		gsub("_GPLOG_","PGExample"i".Passengers.input",Buffer2);
		print(Buffer2)>"PGExample"i".xml";
		close("PGExample"i".xml");
	}
}
