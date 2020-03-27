#include <iostream>
#include "book.h"
#include "heap.h"
#include "compare.h"
using namespace std;


int main(int argc, const char * argv[])
{
    int heaper[10] = {12, 5, 9, 14, 3, 11, 10, 16, 8, 17};
    heap<int, maxintCompare> BH(heaper,10, 20);
    BH.buildHeap();
    for(int i = 0; i < 10; i++)
      cout<<heaper[i]<<", ";
	cout << endl << "The pre-order is; " << endl;
    BH.preOrder(0);
    cout<<endl;
	system("pause");
    return 0;
}