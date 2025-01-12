package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import model.Model;
import model.Point;

public class Game {
	private boolean isBrave;
	private boolean isGloutonne;
	public static int JvJ = 0, JvIA = 1, IAvIA = 2;
	private int variant;
	public static int blanc = 0, bleu = 1, rouge = 2; // si en jvj, bleu = joueur 1 et rouge = joueur 2

	private Model model;

	public Game() {
	}

	/*
	 * fonction lan�ant une partie du jeu.
	 */
	public void play() {
		this.loadFiles();
		this.parametreOfGame();
		if (this.variant == JvIA) {
			this.playIAvJ();
		} else if (this.variant == JvJ) {
			this.playJvJ();
		} else if (this.variant == IAvIA) {
			this.playIAvIA();
		}
	}

	/*
	 * fonction nous permettant de parametrer tout les parametres pour une partie
	 * c�d, la version du jeu, la version de l'ia
	 */
	private void parametreOfGame() {
		System.out.println("Le jeu commence !");
		System.out.println("Vous pouvez jouer en version Brave ou T�m�raire que choisissez vous? (0/1)");
		Scanner scan = new Scanner(System.in);
		int version = -1;
		version = scan.nextInt();
		while (version != 0 && version != 1) {
			System.out.print("Veuillez entrez la valeur 0 pour la version Brave et 1 pour la version T�m�raire.");
			version = scan.nextInt();
		}
		if (version == 0) {
			System.out.println("Vous avez donc choisi la version Brave !");
			this.setBrave(true);
			this.model.actualizingArrayPointsBrave();
		} else {
			System.out.println("Vous avez donc choisi la version T�m�raire !");
			this.setBrave(false);
			this.model.actualizingArrayPointsTemeraire();
		}

		int ia = 0;
		System.out.println("Voulez-vous jouer avec un joueur? 0 , contre une IA? 1, ou laisser deux IA jouer? 2");
		ia = scan.nextInt();
		if (ia != 0) {
			this.setGloutonne(true);
		} else {
			System.out.println("Vous avez donc choisi de jouer en Joueur versus Joueur.");
		}
		if (ia == 0) {
			this.variant = JvJ;
		} else if (ia == 2) {
			this.variant = this.IAvIA;
		} else if (ia == 1) {
			this.variant = this.JvIA;
		}
	}

	private void setGloutonne(boolean b) {
		this.isGloutonne = b;
	}

	/*
	 * Dessine sur le terminal le plateau de jeu
	 */
	private void to_string() {
		this.model.afficher();
	}

	private void endOfGame() {
		System.out.println("Fin de la partie !");
	}
	// **************************************** Fonction qui joue la partie **************************************** //
	/*
	 * @purpose Fonction lan�ant une partie Joueur versus Joueur
	 */
	public void playJvJ() {
		boolean playing = true;
		Scanner scan = new Scanner(System.in);
		int[] move;
		while (playing) {
			this.to_string();

			System.out.println("JOUEUR 1 : ");

			move = this.getBestMove(rouge);
			System.out.println("Le meilleur cout est en : colonne = " + move[0] + ", ligne =" + move[1]);
			int[] coord = chooseCoordinate();
			boolean play;
			play = this.playMove(coord[1], coord[0], rouge);
			while (!play) {
				System.out.println(
						"La case que vous avez selectionn�e est d�j� colori�, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], rouge);
			}
			if (this.model.estTerminee()) {
				playing = false;
				this.to_string();
			} else {
				this.to_string();
				System.out.println("JOUEUR 2 : ");

				move = this.getBestMove(rouge);
				System.out.println("Le meilleur cout est en : colonne = " + move[0] + ", ligne =" + move[1]);
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], bleu);
				while (!play) {
					System.out.println(
							"La case que vous avez selectionn�e est d�j� colori�, veuillez choisir une autre case.");
					coord = chooseCoordinate();
					play = this.playMove(coord[1], coord[0], bleu);
				}
				System.out.println("uncolored nb: " + this.model.getPlateau().getUncolored_nb());
			}
			if (this.model.estTerminee()) {
				playing = false;
				this.to_string();
			}
		}
		scan.close();
		this.endOfGame();

	}

	/*
	 * @purpose Fonction permettant � un joueur de jouer contre une IA
	 */
	public void playIAvJ() {
		boolean playing = true;
		Scanner scan = new Scanner(System.in);
		this.to_string();
		while (playing) {

			System.out.println("Tour du Joueur : ");
			int[] move = this.getBestMove(rouge);
			System.out.println("Le meilleur cout est en : colonne = " + move[0] + ", ligne =" + move[1]);
			int[] coord = chooseCoordinate();
			boolean play = this.playMove(coord[1], coord[0], rouge);
			while (!play) {
				System.out.println(
						"La case que vous avez selectionn�e est d�j� colori�, veuillez choisir une autre case.");
				coord = chooseCoordinate();
				play = this.playMove(coord[1], coord[0], rouge);
			}
			if (this.model.estTerminee()) {
				playing = false;
			} else {
				this.to_string();
				System.out.println("Tour de l'IA : ");
				if (this.isBrave) {
					this.model.JouerGloutonBrave(bleu);
				} else if (!this.isBrave && this.isGloutonne) {
					this.model.JouerGloutonTemeraire(bleu);
				}
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				}
			}

		}
		this.to_string();
	}
	
	/*
	 * @purpose Fonction permettant de lancer une partie avec deux IA
	 */
	public void playIAvIA() {
		boolean playing = true;
		while (playing) {
			if (this.isBrave) {
				System.out.println("Tour de l'IA 1 : ");
				this.model.JouerGloutonBrave(rouge);
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				} else {
					System.out.println("Tour de l'IA 2 : ");
					this.model.JouerGloutonBrave(bleu);
					this.to_string();
					if (this.model.estTerminee()) {
						playing = false;
					}
				}
			} else if (!this.isBrave && this.isGloutonne) {
				System.out.println("Tour de l'IA 1 : ");
				this.model.JouerGloutonTemeraire(rouge);
				this.to_string();
				if (this.model.estTerminee()) {
					playing = false;
				} else {
					System.out.println("Tour de l'IA 2 : ");
					this.model.JouerGloutonTemeraire(bleu);
					if (this.model.estTerminee()) {
						playing = false;
					} else {
						this.to_string();
					}
				}
			}
		}
		this.to_string();

	}

	/*
	 * @purpose Fonction permettant au joueur de jouer un tour
	 * @return boolean si le Move est possible 
	 */
	private boolean playMove(int i, int j, int couleur) {
		if (this.isBrave) {
			return this.model.colorationBrave(i, j, couleur);
		} else {
			return this.model.colorationTemeraire(i, j, couleur);
		}
	}
	
	private int[] getBestMove(int color) {
		int[] res = new int[2];
		Point p;
		if(this.isBrave) {
			p = this.model.EvalCaseBrave(color);
			res[0] = p.getx();
			res[1] = p.gety();
		} else {
			p = this.model.EvalCaseTemeraire(color);
			res[0] = p.getx();
			res[1] = p.gety();
		}
		return res;
	}
	
	/*
	 * @purpose fonction permettant le choix, par l'utilisateur des coordonn�es de son
	 * prochain coup
	 * 
	 * @return int[], le tableau contenant les 2 coordonn�es choisie du joueur
	 */
	private int[] chooseCoordinate() {
		int[] res = new int[2];
		Scanner scan = new Scanner(System.in);
		System.out.print("Veuillez entrez un numero de colonne :");
		res[0] = scan.nextInt();
		while (res[0] > model.getSize() - 1 || res[0] < 0) {
			System.out.println(
					"Veuillez indiquez un num�ro de colonne compris entre 0 et" + model.getSize() + " exclus.");
			res[0] = scan.nextInt();
		}
		System.out.print("Veuillez entrez un numero de ligne :");
		res[1] = scan.nextInt();
		while (res[1] > model.getSize() - 1 || res[1] < 0) {
			System.out.println("Veuillez indiquez un num�ro de ligne entre 0 et" + model.getSize() + " exclus.");
			res[1] = scan.nextInt();
		}
		System.out.println("Vous avez choisi le point en (" + res[0] + "," + res[1] + ")");
		return res;
	}

	/*
	 * @purpose change la valeur du boolean Brave
	 * @param brave, boolean si la version du jeu est brave
	 */
	private void setBrave(boolean brave) {
		isBrave = brave;
	}

	
	
	// **************************************** Fonctions pour charger un fichier ****************************************//
	
	/*
	 * @purpose fonction permettant le chargement d'un fichier txt afin de remplir le
	 * plateau, on peut aussi d�cider de ne pas joindre de fichier et nous devrons
	 * dans ce cas donner un k
	 */
	private void loadFiles() {
		Scanner scan = new Scanner(System.in);

		boolean choose = true;
		boolean preload = false;

		while (choose) {
			System.out.print("Voulez vous charger un plateau (y/n) : ");
			String resp = scan.next();
			System.out.println();

			switch (resp) {
			case "y":
				preload = true;
				choose = false;
				break;
			case "n":
				preload = false;
				choose = false;
				break;
			default:
				System.out.println("Reponse non valide, seuls 'y' et 'n' sont valides");
				break;
			}
		}

		boolean file = preload;
		while (preload) { // donc on load un fichier

			System.out.print("entrez un nom de fichier : ");
			String name = scan.next();
			try {
				remplirPlateau(name);
				preload = false;
				
			} catch (IOException e) {
				System.out.println("Fichier n'existe pas");
				System.out.println("Voulez-vous rentrez un autre nom de fichier? (1 pour oui, 0 pour non)");
				int cast = 0;
				cast = scan.nextInt();
				if(cast == 0) {
					preload = false;
					file = false;
				}
			}

		} if (!file) { // on load rien
			System.out.print("entrez la taille du plateau k (dans la formule 3 * 2 ^ k) : ");
			String k = scan.next();

			this.model = new Model(Integer.parseInt(k));
		}
	}

	
	
	/*
	 * @purpose fonction permettant de lire un fichier txt et de remplir notre plateau avec
	 * les donn�es contenu dans ce fichier
	 */
	public void remplirPlateau(String filename) throws IOException {
		String dir = System.getProperty("user.dir");

		File file = new File(dir + "/" + filename);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String str;
		str = reader.readLine();

		int a = Integer.parseInt(str);
		int b = 0;
		while (a != 3) {
			a = a / 2;
			b++;
		}

		this.model = new Model(b);
		int ligne = 0;

		while ((str = reader.readLine()) != null) {
			for (int col = 0; col < str.length(); col++) {
				switch (str.charAt(col)) {
				case 'R':
					this.model.RemplirTableau(ligne, col, 2);
					break;
				case 'B':
					this.model.RemplirTableau(ligne, col, 1);
					break;
				default:
					break;
				}
			}
			ligne++;
		}
		this.model.RemplirQuadTree(this.model.getQuadTree());
		reader.close();
	}

}
