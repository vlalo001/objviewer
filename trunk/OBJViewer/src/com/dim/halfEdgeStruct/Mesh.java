package com.dim.halfEdgeStruct;

import java.util.ArrayList;

public class Mesh implements Runnable{

	private int[][] faceList;
	private ArrayList<HE_edge> edgeList = new ArrayList<HE_edge>();		
	private ArrayList<pList> PList = new ArrayList<pList>();
	private ArrayList<Integer> ptrPList = new ArrayList<Integer>();
	
	private boolean dataStructureReady = false;
	
	
	//public int[][] faceList = { {0,1,3}, {0,3,2}, {2,3,4} };
	//public int[][] faceList = {{0,6,4}, {0,2,6}, {0,3,2}, {0,1,3}, {2,7,6}, {2,3,7}, {4,6,7}, {4,7,5}, {0,4,5}, {0,5,1}, {1,5,7}, {1,7,3} };
		

	public Mesh(int[][] fl){
		
		this.faceList = fl;
		
		
	}
	
	public boolean isReady(){
		return dataStructureReady;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		/*
		 * Mesh nur für 3eckige Faces definiert
		 */
		
		int i,j,l  = 0;
		i = 0;
		j = 0;
		
						
		for(l = 0; l < faceList.length; l++ ){ //index des Face
			/*
			 * wir gehen davon aus dass die Kante a die erste b die zweite und c die 3. HK sind. 
			 * Auf diesem prinzip beruht folgende berechnung.
			 */			
			HE_edge hk_a = new HE_edge(faceList[l][j], (HE_edge)null, (HE_edge)null, new HE_face(l));			
			HE_edge hk_b = new HE_edge(faceList[l][(j+1)%3], (HE_edge)null, (HE_edge)null, new HE_face(l));
			hk_a.setNext(hk_b);			
			HE_edge hk_c = new HE_edge(faceList[l][(j+2)%3], (HE_edge)null, (HE_edge)null, new HE_face(l));
			hk_b.setNext(hk_c);
			hk_c.setNext(hk_a);			
			
			/* Zufügen zu edgeList */
			edgeList.add(hk_a);
			edgeList.add(hk_b);
			edgeList.add(hk_c);
			
			
			for(i = 0; i < 3; i++){ // i ist index vom punkt
								
				/*
				 * Vorgänger
				 */
				j = (i + 2) % 3; //j ist vorgänger-punkt von i								
				if(faceList[l][j] > faceList[l][i]){ 	//ist der Punkt größer als der PList-Wert(int Indizee v. pList) wird er rausgeschmissen 
					pList pl = new pList(faceList[l][j], new HE_face(l));
					//auf vorhandene gegenkante prüfen
					if(!plCompare(pl,faceList[l][i]))
					{
						//System.out.println("pl: " + pl);
						PList.add(pl);	//speichern des pList Objekts in einer Liste				
						ptrPList.add(faceList[l][i]);	//speichern des Indizees - quasi Startpunkt
																		
					}
					else{
						/*
						 * Da hier eine innere Kante gefunden wurde, ist dies eine Gegenkante einer in der
						 * edgeList bereits vorhandenen kannte mit parameter pair_edge == null
						 * In der for Schleife sucht man die Gegenkante die den Endpunkt v. pl (faceList[l][j]) hat
						 */						
						HE_edge pair_edge = null;
						for(int z = edgeList.size()-1; z > (edgeList.size()-4); z--){
							if(edgeList.get(z).getVert() == faceList[l][i])
								pair_edge = edgeList.get(z);
						}
						
						findAndSetPairEdge(faceList[l][j], pair_edge);
						
						
					}
					
				}
				
				
				/*
				 * Nachfolger
				 */
				j = (i + 4) % 3; //j ist nachfolger-punkt von i								
				if(faceList[l][j] > faceList[l][i]){
					pList pl_ = new pList(faceList[l][j], new HE_face(l));
					if(!plCompare(pl_,faceList[l][i]))
					{
						//System.out.println("pl: " + pl_);
						PList.add(pl_);	//speichern des pList Objekts in einer Liste				
						ptrPList.add(faceList[l][i]);	//speichern des Indizees
						
					}
					else{
						/*
						 * Da hier eine innere Kante gefunden wurde, ist dies eine Gegenkante einer in der
						 * edgeList bereits vorhandenen kannte mit parameter nextedge = null
						 */
						
						HE_edge pair_edge = null;
						for(int z = edgeList.size()-1; z > (edgeList.size()-4); z--){
							if(edgeList.get(z).getVert() == faceList[l][j])
								pair_edge = edgeList.get(z);								
						}
												
							
						findAndSetPairEdge(faceList[l][i], pair_edge);
						
						
					}
				}
						
			}//inner for			
			
			
		}

		
				
		/**
		 * Gibt geordnete Liste der pList Elemente aus
		 */
		/*
		for(int x = 0; x < PList.size(); x++){			
			for(int y = 0; y < ptrPList.size(); y++){
				if(ptrPList.get(y) == x ){
					System.out.print("p[" + ptrPList.get(y) + "] = " + " pList: " + PList.get(y));
					System.out.println("");
				}
			}			
		}
		
		*/
		
		for(int x = 0; x < edgeList.size(); x++){
			System.out.println(x + ": " + "edge: " + edgeList.get(x));			
		}
		System.out.println(edgeList.size());
		
		this.dataStructureReady = true;
	}	
	
	/**
	 * Suche und setze die PairEdge aus der edgeList
	 * @param begin_point faceList[l][i]
	 * @param edge	Pair Edge
	 * @return
	 */
	public boolean findAndSetPairEdge(int begin_point, HE_edge edge){
		for(HE_edge e: edgeList){
			if(e.getVert() == begin_point){
				if(e.getNext().getNext().getVert() == edge.getVert()){							
					
					e.setPair(edge);
					edge.setPair(e);
										
					return true;
				}				
			}
		}
				
		return false;
	}

	/**
	 * Wir suchen nach einer Gegenkante in PList Liste. Bei Fund return true. 
	 * @param pl
	 * @param index Startpunkt im Face 
	 * @return true wenn gefunden, false wenn nicht 
	 */
	public boolean plCompare(pList pl, int index){
		int i = 0;
		int k = 0;
		
		for(i = 0; i < PList.size(); i++){			
			if(PList.get(i).getPoint() == pl.getPoint() && index == ptrPList.get(i))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Gibt alle Punkte von Außenkanten zurück. Erst Anfangspunkt der Edge dann Endpunkt usw.
	 * @return
	 */
	public int[] getBorderEdgePoints(){
		ArrayList<Integer> points = new ArrayList<Integer>();
		for(HE_edge e : edgeList){
			if(e.getPair() == null){
				points.add(e.getNext().getNext().getVert());
				points.add(e.getVert());
			}				
		}
		
		int[] pointsArr = new int[points.size()];
		int i = 0;
		for(int p: points){
			pointsArr[i] = p;
			i++;
			System.out.print(p+",");
		}
		
		
		return pointsArr;
	}


		
}
