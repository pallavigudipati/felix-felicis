#include <iostream>

#include "simulator.cpp"

using namespace std;

main() {
	Simulator simulator;
	simulator.LoadSpecifications();
	simulator.RunSimulation();
}
