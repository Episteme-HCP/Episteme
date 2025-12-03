BEGIN		{
	while(getline < "KMSkeleton.cs") {
		Buffer=Buffer""$0"\n";
	}
	exit;
}

//		{
	;
}

END		{
	KM[0]="\t\t\t<SimpleKinematicModel BaseTime=\"30\" DoorCloseTime=\"30\" DoorOpenTime=\"20\" FloorFligthTime=\"20\"/>\n";
	KM[1]="\t\t\t<SimpleKinematicModel BaseTime=\"30\" DoorCloseTime=\"10\" DoorOpenTime=\"10\" FloorFligthTime=\"20\"/>\n";

	LSA1="\t\t\t\t<LoadSpeed BaseTime=\"15\" DoorCloseTime=\"30\" DoorOpenTime=\"20\" FloorFligthTime=\"5\" FromLoad=\"0\" ToLoad=\"0\"/>";
	LSA2="\t\t\t\t<LoadSpeed BaseTime=\"30\" DoorCloseTime=\"30\" DoorOpenTime=\"20\" FloorFligthTime=\"10\" FromLoad=\"1\" ToLoad=\"36\"/>";

	LSB1="\t\t\t\t<LoadSpeed BaseTime=\"15\" DoorCloseTime=\"10\" DoorOpenTime=\"10\" FloorFligthTime=\"5\" FromLoad=\"0\" ToLoad=\"0\"/>";
	LSB2="\t\t\t\t<LoadSpeed BaseTime=\"30\" DoorCloseTime=\"10\" DoorOpenTime=\"10\" FloorFligthTime=\"10\" FromLoad=\"1\" ToLoad=\"36\"/>";

	KM[2]="\t\t\t<LoadDependentKinematicModel>\n\t\t\t\t"LSA1"\n"LSA2"\n\t\t\t</LoadDependentKinematicModel>";
	KM[3]="\t\t\t<LoadDependentKinematicModel>\n\t\t\t\t"LSB1"\n"LSB2"\n\t\t\t</LoadDependentKinematicModel>";

	for (i=0;i<720;i++) {
		NC=int((i/240)+2);
		CC=(int((i%240)/80)+2)*3;
		KT=int(i/20)%4;
		PG=(i%20)+1;

		j=i+1;

		Cars="";
		for (z=0;z<NC;z++) {
			ACar="\t\t<Car StartingFloor=\"0\" SpaceCapacity=\""CC"\" WeightCapacity=\""(CC*80)"\">\n";
			ACar=ACar""KM[KT]"\t\t</Car>\n";
			Cars=Cars""ACar;
		}

                Buffer2=Buffer;
                gsub("_NAME_","Kinematic Models Example ("j"/720)",Buffer2);

                gsub("_CARS_",Cars,Buffer2);
                gsub("_INPUT_","PGExample"PG".Passengers.input",Buffer2);
                gsub("_OUTPUT_","KMExample"j".output.csv",Buffer2);
#                print(Buffer2);
                print(Buffer2)>"KMExample"j".xml";
                close("KMExample"i".xml");
	}
}
