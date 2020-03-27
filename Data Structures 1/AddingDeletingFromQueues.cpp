#include<queue>
#include<iostream>
using namespace std;

queue<int> Q;
void addToQ(int x)
{
	Q.push(x);
	if (Q.size() > 12)
	{
		cout << "Max size reached. Deleting first element..." << endl;
		Q.pop();
	}
}
int main() {


	addToQ(2);
	addToQ(4);
	addToQ(6);
	addToQ(7);
	addToQ(5);
	addToQ(3);
	cout << "Currently there are " << Q.size() << " elements in the queue." << endl;
	cout << "The first element (in front) is: " << Q.front() << endl;
	cout << "The element in the back is: " << Q.back() << endl<<endl;
	cout << Q.front() << " has been popped." << endl << endl;
	Q.pop();
	cout << Q.front() << " has been popped." <<endl<< endl;
	Q.pop();
	cout << "Now there are " << Q.size() << " elements in the queue." << endl;
	cout << "The first element (in front) is: " << Q.front() << endl;
	cout<<"The element in the back is: "<< Q.back() << endl;

	system("pause");
	return 0;
}