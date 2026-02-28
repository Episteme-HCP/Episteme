<?xml version="1.0" encoding="UTF-8" ?>
<!-- This file is licensed under the GPL. Author: Nagy Elemer Karoly (eknagy@users.sourceforge.net) -->
<!-- $Revision: 1.1 $ $Date: 2006-09-07 22:00:55 $ -->
<!DOCTYPE JLESAcfg PUBLIC "" "JLESAcfg.dtd" >
<JLESAcfg name="_NAME_" author="Nagy Elemér Károly" created="Dynamic">
	<Building HighestFloor="6" LowestFloor="-1" MaxNumberOfTicks="40000" TickBlocks="5000"/>
	<GUI Visible="false" AnimationSpeed="5"/>
	<CA>
		<Car StartingFloor="0" SpaceCapacity="20" WeightCapacity="450">
			<SimpleKinematicModel BaseTime="1" DoorCloseTime="1" DoorOpenTime="1" FloorFligthTime="1"/>
		</Car>
		<Car StartingFloor="0" SpaceCapacity="20" WeightCapacity="450">
			<SimpleKinematicModel BaseTime="1" DoorCloseTime="1" DoorOpenTime="1" FloorFligthTime="1"/>
		</Car>
		<Car StartingFloor="0" SpaceCapacity="20" WeightCapacity="450">
			<SimpleKinematicModel BaseTime="1" DoorCloseTime="1" DoorOpenTime="1" FloorFligthTime="1"/>
		</Car>
		<Car StartingFloor="0" SpaceCapacity="20" WeightCapacity="450">
			<SimpleKinematicModel BaseTime="1" DoorCloseTime="1" DoorOpenTime="1" FloorFligthTime="1"/>
		</Car>
		<DynZoneCA VisibleGUI="false"/>
	</CA>
	<PoissonPassengerGenerator RandomSeed="_RS_">
		<GammaLine Floor="6"	Gamma="_F6G_"/>
		<GammaLine Floor="5"	Gamma="_F5G_"/>
		<GammaLine Floor="4"	Gamma="_F4G_"/>
		<GammaLine Floor="3"	Gamma="_F3G_"/>
		<GammaLine Floor="2"	Gamma="_F2G_"/>
		<GammaLine Floor="1"	Gamma="_F1G_"/>
		<GammaLine Floor="0"	Gamma="_F0G_"/>
		<GammaLine Floor="-1"	Gamma="_F-1G_"/>
	</PoissonPassengerGenerator>
	<LoggerPassengerProcessor GeneratedPassengersOutputFile="_GPLOG_" PassengerTimesOutputFile="PassengerTimes.log" QueuLengthsOutputFile="QueuLengths.log"/>
</JLESAcfg>
