#include<iostream>
#include<math.h>

#include <cstdio>

#include<chrono>
using namespace std;
using std::chrono::nanoseconds;
using std::chrono::steady_clock;


long fibr(int n) {


	if ((n == 1) || (n == 2)) return 1;
	return fibr(n - 1) + fibr(n - 2);

}

int main() {
	int a;
	long long duration;
	
	cout << "Please input a number to convert it in fibonacci" << endl;
	cin >> a;

	while (a != 0) {
		auto start = get_time::now();
		

		cout << fibr(a) << endl;
		auto end = get_time::now();
		auto diff = end - start;
		
		cout << chrono::duration_cast<ns>(diff).count() << " nanoseconds is the time it took." << endl;
		cout << "Please input a number to convert it in fibonacci" << endl;
		cin >> a;
		
	}
	return 0;
}