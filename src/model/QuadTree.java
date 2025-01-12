package model;

public class QuadTree {
	private Integer value;
	private Point point;
	private QuadTree qt0, qt1, qt2, qt3;
	private QuadTree father;
	private boolean isSterile;
	private int size;
	public static int blanc = 0, bleu = 1, rouge = 2;

	/*
	 * @purpose construit le QuadTree
	 * @complexity O(1)
	 */
	public QuadTree() {
		this.size = 3;
		this.isSterile = false;
		this.father = null;
		this.value = null;
		this.point = null;
		this.qt0 = null;
		this.qt1 = null;
		this.qt2 = null;
		this.qt3 = null;
	}

	/*
	 * Constructeur de QuadTree
	 * @param value, la couleur du QuadTree
	 * @param point, le point en haut � gauche de la zone
	 * @param qt0, qt1, qt2 et qt3, les 4 sous-zones. 
	 * qt0 la zone haut gauche, qt1 la zone haut droite, qt2 la zone basse droite et qt3 la zone basse gauche
	 * @commplexity  O(1)
	 */
	public QuadTree(QuadTree father, boolean isSterile,Integer value, Point point, QuadTree qt0, QuadTree qt1, QuadTree qt2, QuadTree qt3, int size) {
		this.size = size;
		this.father = father;
		this.isSterile = isSterile;
		this.value = value;
		this.point = point;
		this.qt0 = qt0;
		this.qt1 = qt1;
		this.qt2 = qt2;
		this.qt3 = qt3;
	}
	
	/*
	 * @purpose renvoie la taille du cot� du quadTree courant
	 * @complexity O(1)
	 */
	public int getSize() {
		return this.size;
	}
	
	/*
	 * @purpose change la valeur de l'attribut isSterile du QuadTree (Quand une r�gione est captur�e par exemple)
	 * @complexity O(1)
	 */
	public void setIsSterile(boolean isSterile) {
		this.isSterile = isSterile;
	}
	
	/*
	 * @purpose renvoie la valeur de l'attribut isSteriel du QuadTree
	 * @complexity O(1)
	 */
	public boolean getIsSterile() {
		return this.isSterile;
	}
	
	public void setValue(Integer newValue) {
		this.value = newValue;
	}

	public void setPoint(Point newPoint) {
		this.point = newPoint;
	}

	public QuadTree getQt(int index) {
		QuadTree ret = null;

		switch (index) {
		case 0:
			ret = this.qt0;
			break;

		case 1:
			ret = this.qt1;
			break;

		case 2:
			ret = this.qt2;
			break;

		case 3:
			ret = this.qt3;
			break;

		default:
			System.out.println("Pas le bon choix d'index, doit etre compris entre 0 et 3 (inclus)");
		}
		return ret;
	}
	
	public void deleteSons() {
		this.qt0 = null;
		this.qt1 = null;
		this.qt2 = null;
		this.qt3 = null;
	}

	public int getValue() {
		return this.value;
	}

	public Point getPoint() {
		return this.point;
	}
	
	public boolean getisSterile() {
		return this.isSterile;
	}

	public QuadTree getFather() {
		return this.father;
	}
	public void printValuePoint() {
		if (this.point != null && this.value != null) {
			System.out.println(this.value + ";" + this.point.getx() + ";" + this.point.gety());
		}
		if (this.qt0 != null) {
			qt0.printValuePoint();
		}
		if (this.qt1 != null) {
			qt1.printValuePoint();
		}
		if (this.qt2 != null) {
			qt2.printValuePoint();
		}
		if (this.qt3 != null) {
			qt3.printValuePoint();
		}
	}
	
	public int getValue(int ligne, int col) {
		if(this.isSterile) {
			return this.value;
		} else {
			if (col >= this.getQt(2).getPoint().getx()) {
				if (ligne >= this.getQt(2).getPoint().gety()) {
					this.getQt(2).getValue(ligne, col);
				} else {
					this.getQt(1).getValue(ligne, col);
				}
			} else {
				if (ligne >= this.getQt(2).getPoint().gety()) {
					this.getQt(3).getValue(ligne, col);
				} else {
					this.getQt(0).getValue(ligne, col);
				}
			}
		}
		return 0;
	}	
}
