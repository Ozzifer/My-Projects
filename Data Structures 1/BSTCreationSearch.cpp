#include<iostream>
#include<string>
using namespace std;

class obj{
public:
	string city;
	int x, y;
	obj(){};
	obj(string s, int i, int k)
	{
		city = s;
		x = i;
		y = k;
	}
};
// Binary Search Tree implementation for the Dictionary ADT
template <typename Key, typename Elem>
class BST : public Dictionary<Key,Elem> {
	private:
		BNode<Key,Elem>* root; // Root of the BST
		int nodecount; // Number of nodes in the BST
		// Private "helper" functions
		void clearhelp(BNode<Key, Elem>*);
		BNode<Key,Elem>* inserthelp(BNode<Key, Elem>*,const Key&, const Elem&);
		BNode<Key,Elem>* deletemin(BNode<Key, Elem>*,BNode<Key, Elem>*&);
		BNode<Key,Elem>* removehelp(BNode<Key, Elem>*, const Key&,BNode<Key, Elem>*&);
		bool findhelp(BNode<Key, Elem>*, const Key&, Elem&) const;
		void printhelp(BNode<Key, Elem>*, int) const;
	public:
		BST() { root = NULL; nodecount = 0; } // Constructor
		˜BST() { clearhelp(root); } // Destructor
		void clear() // Reinitialize dictionary
		{ clearhelp(root); root = NULL; nodecount = 0; }
		// Insert Elem "it" with Key "k" into the dictionary.
		void insert(const Key& k, const Elem& it) {root = inserthelp(root, k, it);
		nodecount++;
}
// Remove some record with key "K" and return it in "it".
// Return true if such exists, false otherwise.
// If multiple records match "K", remove an arbitrary one.
bool remove(const Key& K, Elem& it) {
	BNode<Key,Elem>* t = NULL;
	root = removehelp(root, K, t);
	if (t == NULL) return false; // Nothing found
	it = t->val();
	nodecount--;
	delete t;
	return true;
	}
// Remove and return the record with smallest key value
// from the dictionary and return in "it". Return true if
// some record is removed, false otherwise.
Elem removeAny() { // Delete min value
	Assert(root != NULL, "Empty tree");
	BNode<Key, Elem>* t;
	root = deletemin(root, t);
	Elem it = t->val();
	delete t;
	nodecount--;
	return it;
}
// Return a copy in "it" of some record matching "K".
// Return true if such exists, false otherwise. If
// multiple records match "K", return an arbitrary one.
bool BST<Key, Elem>::findhelp(BNode<Key, Elem>* root,const Key& K, Elem& e) const {
	if (root == NULL) return false; // Empty tree
	else if (K < root->key())
	return findhelp(root->left(), K, e); // Check left
	else if (K > root->key())
	return findhelp(root->right(), K, e); // Check right
	else { e = root->val(); return true; } // Found it
}
BNode<Key, Elem>* BST<Key, Elem>::inserthelp(BNode<Key, Elem>* root, const Key& k, const Elem& it) {
	if (root == NULL) // Empty tree: create node
	return new BNode<Key, Elem>(k, it, NULL, NULL);
	if (k < root->key())
	root->setLeft(inserthelp(root->left(), k, it));
	else root->setRight(inserthelp(root->right(), k, it));
	return root; // Return tree with node inserted
}

};




int main() {
	obj city1(Atlanta, 10, 240);
	obj city2(Austin, 100, 200);
	obj city3(Boston, 200, 300);
	obj city4(Chicago, 360, 890);
	obj city5(CedarRapids, 205, 400);
	obj city6(Dallas, 300, 450);
	obj city7(Detroit, 150, 360);
	obj city8(NewYork, 50, 270);
	obj city9(LosAngeles, 600, 800);
	obj city10(LasVegas, 580 780);
	obj city11(Portland, 650, 830);
	obj city12(Seattle, 680, 780);
	obj city13(SanFrancisco, 600, 800);
	BST tree;
	tree.insert("Atlanta", city1);






	return 0;
}
