/*  
 *   Authors: Caroline Appert (caroline.appert@lri.fr)
 *   Copyright (c) Universite Paris-Sud XI, 2007. All Rights Reserved
 *   Licensed under the GNU LGPL. For full terms see the file COPYING.
 */
package fr.lri.swingstates.gestures.rubine;

/*******************************************************************************
 * The algorithm and original C code are: (C) Copyright, 1990 by Dean Rubine,
 * Carnegie Mellon University Permission to use this code for noncommercial
 * purposes is hereby granted. Permission to copy and distribute this code is
 * hereby granted provided this copyright notice is retained. All other rights
 * reserved.
 ******************************************************************************/

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import fr.lri.swingstates.canvas.CPolyLine;
import fr.lri.swingstates.gestures.AbstractClassifier;
import fr.lri.swingstates.gestures.Gesture;
import fr.lri.swingstates.gestures.GestureClass;
import fr.lri.swingstates.gestures.GestureUtils;
import fr.lri.swingstates.gestures.Score;

/**
 * A classifier that implements rubine's algorithm to classify gestures.
 * 
 * @author Caroline Appert
 * 
 */
public class RubineClassifier extends AbstractClassifier {
	
	class RubineGestureClass extends GestureClass {

		private Vector<Double> average;
		private Matrix sumCov;

		RubineGestureClass() {
			super();
			average = new Vector<Double>(RubineClassifier.NAME_FEATURES.length);
			for (int i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				average.add(0.0);
			sumCov = new Matrix(RubineClassifier.NAME_FEATURES.length, RubineClassifier.NAME_FEATURES.length, true);
		}

		RubineGestureClass(String n) {
			this();
			name = n;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean removeExample(Gesture gesture) {
			if (!gestures.contains(gesture))
				return false;

			int i, j;
			int nExamples = gestures.size();

			double[] nfv = new double[RubineClassifier.NAME_FEATURES.length];
			Vector<Double> fv = RubineClassifier.getFeatures(gesture);

			double nm1on = ((double) nExamples - 1) / nExamples;
			double recipn = 1.0 / nExamples;

			/* incrementally update mean vector */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				average.set(i, (average.get(i) - recipn * fv.get(i)) / nm1on);

			/* incrementally update covariance matrix */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				nfv[i] = fv.get(i) - average.get(i);

			/* only upper triangular part computed */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				for (j = i; j < RubineClassifier.NAME_FEATURES.length; j++)
					sumCov.items[i][j] -= nm1on * nfv[i] * nfv[j];

			return super.removeExample(gesture);
		}

		/**
		 * {@inheritDoc}
		 */
		public void addExample(Gesture gesture) {
			super.addExample(gesture);
			int i, j;
			double[] nfv = new double[RubineClassifier.NAME_FEATURES.length];
			Vector<Double> fv = RubineClassifier.getFeatures(gesture);
			int nExamples = gestures.size();

			double nm1on = ((double) nExamples - 1) / nExamples;
			double recipn = 1.0 / nExamples;

			/* incrementally update covariance matrix */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				nfv[i] = fv.get(i) - average.get(i);
			/* only upper triangular part computed */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				for (j = i; j < RubineClassifier.NAME_FEATURES.length; j++)
					sumCov.items[i][j] += nm1on * nfv[i] * nfv[j];
			/* incrementally update mean vector */
			for (i = 0; i < RubineClassifier.NAME_FEATURES.length; i++)
				average.set(i, nm1on * average.get(i) + recipn * fv.get(i));

		}

		/**
		 * @return the average vector features of this gesture class.
		 */
		public Vector<Double> getAverage() {
			return average;
		}

		/**
		 * @return the covariance matrix of this gesture class.
		 */
		public Matrix getSumCov() {
			return sumCov;
		}

	}


	private static double EPSILON = 1.0e-6;

	static double DIST_SQ_THRESHOLD = 3 * 3;
	static double SE_TH_ROLLOFF = 4 * 4;

	/**
	 * index of <i>cosinus initial angle</i> (angle between first and third points) in a vector of features.
	 */
	public static int PF_INIT_COS = 0;
	/**
	 * index of <i>sinus initial angle</i> (angle between first and third points) in a vector of features.
	 */
	public static int PF_INIT_SIN = 1;
	/**
	 * index of <i>length of bounding box diagonal</i> in a vector of features.
	 */
	public static int PF_BB_LEN = 2;
	/**
	 * index of <i>angle of bounding box diagonal</i> in a vector of features.
	 */
	public static int PF_BB_TH = 3;
	/**
	 * index of <i>length between start and end points</i> in a vector of features.
	 */
	public static int PF_SE_LEN = 4;
	/**
	 * index of <i>cosinus of angle between start and end points</i> in a vector of features.
	 */
	public static int PF_SE_COS = 5;
	/**
	 * index of <i>sinus of angle between start and end points</i> in a vector of features.
	 */
	public static int PF_SE_SIN = 6;
	/**
	 * index of <i>arc length of path</i> in a vector of features.
	 */
	public static int PF_LEN = 7;
	/**
	 * index of <i>total angle traversed</i> in a vector of features.
	 */
	public static int PF_TH = 8;
	/**
	 * index of <i>sum of absolute values</i> of angles traversed) in a vector of features.
	 */
	public static int PF_ATH = 9;
	/**
	 * index of <i>sum of squares of angles</i> traversed) in a vector of features.
	 */
	public static int PF_SQTH = 10;
	/**
	 * index of <i>path duration</i> in a vector of features.
	 */
	public static int PF_DUR = 11;
	/**
	 * index of <i>maximum speed</i> in a vector of features.
	 */
	public static final int PF_MAXV = 12;

	public static String[] NAME_FEATURES = {
		"initial angle cosinus                  ",
		"initial angle sinus                    ",
		"bounding box length                    ",
		"bounding box diagonal angle            ",
		"distance      [start, end]             ",
		"cosinus angle [start, end]             ",
		"sinus angle   [start, end]             ",
		"arc length of path                     ",
		"total angle traversed                  ",
		"sum absolute values of angles traversed",
		"sum of squares of angles traversed     ",
		"path duration                          ",
		"maximum speed                          "
	};


	private ArrayList<Double> cnst = null;
	private Vector<Vector<Double>> weights = null;
	private Matrix invAvgCov;
	private boolean compiled;

//	private double probabilityThreshold = 1.0;
	private int mahalanobisThreshold = 1000;

//	private transient double currentProbability = 0;
	private transient double currentDistance = 0;

	protected ArrayList<RubineGestureClass>    classes = new ArrayList<RubineGestureClass>();

	/**
	 * Builds a new classifier by loading its definition in a file.
	 * 
	 * @param file
	 *            The name of the file containing the definition of the
	 *            classifier.
	 * @return The newly created classifier.
	 */
	public static RubineClassifier newClassifier(String file) {
		return newClassifier(new File(file));
	}

	/**
	 * Builds a new classifier by loading its definition in a url.
	 * 
	 * @param url
	 *            The url containing the definition of the classifier.
	 * @return The newly created classifier.
	 */
	public static RubineClassifier newClassifier(URL url) {
		RubineClassifier c = new RubineClassifier();
		try {
			URLConnection urlc = null;
			urlc = url.openConnection();
			c.read(new DataInputStream(urlc.getInputStream()));
			c.compile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}

	private double mahalanobisDistance(Vector<Double> v, Vector<Double> u, Matrix sigma) {
		Vector<Double> tmp = VectorUtility.minus(v, u);
		double quadForm = VectorUtility.quadraticForm(tmp, sigma);
		return quadForm;
	}

	private void fix(Matrix avgcov) {
		double[] det = new double[1];
		BitVector bv = new BitVector();
		// just add the features one by one, discarding any that cause the
		// matrix to be non-invertible
		bv.zero();
		for (int i = 0; i < NAME_FEATURES.length; i++) {
			bv.set(i);
			avgcov.slice(bv, bv).invert(det);
			if (Math.abs(det[0]) <= EPSILON)
				bv.clear(i);
		}
		Matrix m = avgcov.slice(bv, bv).invert(det);
		if (Math.abs(det[0]) <= EPSILON)
			System.err.println("Can't fix classifier!\n");

		invAvgCov = m.deSlice(0.0, NAME_FEATURES.length, NAME_FEATURES.length, bv, bv);
	}

	/**
	 * Builds a new classifier by loading its definition in a file.
	 * 
	 * @param filename
	 *            The name of the file containing the definition of the
	 *            classifier.
	 * @return The newly created classifier.
	 */
	public static RubineClassifier newClassifier(File filename) {
		RubineClassifier c = new RubineClassifier();
		try {
			c.read(new DataInputStream(new FileInputStream(filename)));
			c.compile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return c;
	}

	/**
	 * {@inheritDoc}
	 */
	public void reset() {
		cnst = null;
		weights = null;
		init();
		classes.clear();
		super.reset();
		compiled = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeClass(String className) {
		int index = classesNames.indexOf(className);
		if(index == -1) return;
		classes.remove(index);
		weights.remove(index);
		cnst.remove(index);
		super.removeClass(className);
		compiled = false;
	}
	
	/**
	 * Returns a graphical representation for a given class of gestures. The
	 * graphical representation is the one which minimizes the distance with
	 * vector of features characterizing this gesture class.
	 * 
	 * @param className
	 *            the name of the gesture class.
	 * @return A representative polyline for the gesture class having name
	 *         <code>className</code>.
	 */
	public CPolyLine getRepresentative(String className) {
		compile();
		int i = 0;
		GestureClass gestureClass = null;

		for (; i < classes.size(); i++) {
			gestureClass = classes.get(i);
			if (className.compareTo(gestureClass.getName()) == 0)
				break;
		}

		if (i >= classes.size())
			return null;

		CPolyLine representative = null;
		Gesture next = null;
		double maxValue = Double.MIN_VALUE;

		for (Iterator<Gesture> iterator = gestureClass.getGestures().iterator(); iterator.hasNext();) {
			next = iterator.next();
			Vector<Double> fv = getFeatures(next);
			double value = VectorUtility.scalarProduct(weights.get(i), fv) + cnst.get(i);
			if (value > maxValue) {
				maxValue = value;
				representative = next.asPolyLine();
			}
		}
		if (representative != null)
			representative.setFilled(false);
		return representative;
	}

	void compute() {
		if (classes.size() == 0)
			return;

		Matrix avgcov = new Matrix(NAME_FEATURES.length, NAME_FEATURES.length, true);
		int ne = 0;
		int nc;
		ArrayList<RubineGestureClass> d = classes;

		for (nc = 0; nc < classes.size(); nc++) {
			ne += d.get(nc).getNumberOfExamples();
			/* should do : avgcov += d->Sumcov; for triangular */
			for (int i = 0; i < NAME_FEATURES.length; i++)
				for (int j = i; j < NAME_FEATURES.length; j++)
					avgcov.items[i][j] += d.get(nc).getSumCov().items[i][j];
		}

		int denom = ne - classes.size();
		if (denom <= 0) {
			System.out.println("no examples, denom=" + denom + "\n");
			return;
		}

		double oneoverdenom = 1.0 / denom;
		/* should have : avgcov *= oneoverdenom and detriangularize */
		for (int i = 0; i < NAME_FEATURES.length; i++)
			for (int j = i; j < NAME_FEATURES.length; j++) {
				avgcov.items[i][j] *= oneoverdenom;
				avgcov.items[j][i] = avgcov.items[i][j];
			}

		/* invert the avg covariance matrix */
		double[] det = new double[1];

		invAvgCov = avgcov.invert(det);
		if (Math.abs(det[0]) <= EPSILON)
			fix(avgcov);

		/* now compute discrimination functions */
		d = classes;

		init();

		Vector<Vector<Double>> w = weights;
		ArrayList<Double> cst = cnst;
		for (nc = 0; nc < classes.size(); nc++) {
			if (nc >= w.size())
				w.add(VectorUtility.mult(d.get(nc).getAverage(), invAvgCov));
			else
				w.set(nc, VectorUtility.mult(d.get(nc).getAverage(), invAvgCov));
			cst.set(nc, -0.5 * VectorUtility.scalarProduct(w.get(nc), d.get(nc).getAverage()));
			/* could add log(priorprob class) to cnst */
		}
		compiled = true;
	}

	private void init() {
		if (cnst == null) {
			cnst = new ArrayList<Double>();
			for (int i = 0; i < classes.size(); i++)
				cnst.add(0.0);
		}
		if (weights == null) {
			weights = new Vector<Vector<Double>>();
			for (int i = 0; i < classes.size(); i++)
				weights.add(new Vector<Double>(NAME_FEATURES.length));
		}
	}

	/**
	 * Compiles this classifier (i.e. performs training).
	 */
	private void compile() {
		if (!compiled)
			compute();
	}

	/**
	 * Recognizes a gesture.
	 * 
	 * @param g
	 *            The gesture to recognize
	 * @return The class of gestures that best fit to g.
	 */
	public String classify(Gesture g) {
		compile();

		Vector<Double> fv = getFeatures(g);
		Vector<Vector<Double>> wghts = weights;
		ArrayList<Double> cst = cnst;
		ArrayList<RubineGestureClass> d = classes;
		double[] values = new double[classes.size()];
		RubineGestureClass maxclass = null;
		double maxvalue = -Double.MAX_VALUE;

		for (int nc = 0; nc < classes.size(); nc++) {
			double value = VectorUtility.scalarProduct(wghts.get(nc), fv) + cst.get(nc);
			values[nc] = value;
			if (value > maxvalue) {
				maxclass = d.get(nc);
				maxvalue = value;
			}
		}

		/* compute probability of non-ambiguity */
//		double denom = 0.;
//		for (int i = 0; i < classes.size(); i++) {
//		double delta = values[i] - maxvalue;
//		denom += Math.exp(delta);
//		}
//		currentProbability = 1.0 / denom;

		// calculate distance to mean of chosen class
		currentDistance = mahalanobisDistance(fv, maxclass.getAverage(), invAvgCov);

//		System.out.println("currentProbability: " + currentProbability);
//		System.out.println("probabilityThreshold: " + probabilityThreshold);
//		System.out.println("currentDistance: " + currentDistance);
//		System.out.println("mahalanobisThreshold: " + mahalanobisThreshold);
//		if (currentProbability >= probabilityThreshold && currentDistance <= mahalanobisThreshold)
//		return maxclass.getName();
		if (currentDistance <= mahalanobisThreshold)
			return maxclass.getName();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int addClass(String className) {
		int index = super.addClass(className);
		if(index == -1) return -1;
		RubineGestureClass gcr = new RubineGestureClass(className);
		classes.add(gcr);
		init();
		weights.add(new Vector<Double>(NAME_FEATURES.length));
		cnst.add(0.0);
		compiled = false;
		return index;
	}

	/**
	 * Computes the vector of features for a given class of gestures.
	 * <ul>
	 * <li>(0) cosinus of initial angle</li>
	 * <li>(1) sinus of initial angle</li>
	 * <li>(2) length of bounding box diagonal</li>
	 * <li>(3) angle of bounding box diagonal</li>
	 * <li>(4) length between start and end points</li>
	 * <li>(5) cosinus of angle between start and end points</li>
	 * <li>(6) sinus of angle between start and end points</li>
	 * <li>(7) arc length of path</li>
	 * <li>(8) total angle traversed</li>
	 * <li>(9) sum of abs vals of angles traversed</li>
	 * <li>(10) sum of squares of angles traversed</li>
	 * <li>(11) duration of path</li>
	 * <li>(12) maximum speed</li>
	 * </ul>
	 * 
	 * @param className
	 *            The name of the class of gesture.
	 * @return The vector of features for the <code>className</code> class of
	 *         gestures, null if this class does not exist.
	 */
	public Vector<Double> getFeatures(String className) {
		int index = classesNames.indexOf(className);
		if(index == -1) return null;
		RubineGestureClass gClass = classes.get(index);
		return gClass.getAverage();
	}

	/**
	 * @param className
	 *            The name of the class of gesture.
	 * @return The number of examples in <code>className</code> class of
	 *         gestures, -1 if this class does not exist..
	 */
	public int getNbGestureExamples(String className) {
		int index = classesNames.indexOf(className);
		if(index == -1) return -1;
		RubineGestureClass gClass = classes.get(index);
		return gClass.getNumberOfExamples();
	}

	protected void write(DataOutputStream out) throws IOException {
		out.writeInt(classes.size());
		for (int i = 0; i < classes.size(); i++)
			classes.get(i).write(out);
	}
	
	protected Object read(DataInputStream in) throws IOException {
		int nClasses = in.readInt();
		for (int i = 0; i < nClasses; i++) {
			RubineGestureClass c = new RubineGestureClass();
			c.read(in);
			classes.add(c);
			classesNames.add(c.getName());
			templates.add(null);
		}

		return this;
	}

	/**
	 * @return The probability of non ambiguity for the last recognized gesture.
	 */
//	public double getCurrentProbability() {
//	return currentProbability;
//	}

	/**
	 * @return The distance of the last recognized gesture to the average vector
	 *         of its gesture class.
	 */
	public double getCurrentDistance() {
		return currentDistance;
	}

	/**
	 * Sets a minimum probability of non ambiguity for recognition. If the
	 * probability is less, the gesture is non recognized (i.e. method
	 * <code>classify</code> returns null). By default, this probability is
	 * set to 1.
	 * 
	 * @param proba
	 *            The minimum probability of non ambiguity.
	 */
//	public void setMinimumProbability(double proba) {
//	probabilityThreshold = proba;
//	}

	/**
	 * @return The minimum probability of non ambiguity for recognition.
	 */
//	public double getMinimumProbability() {
//	return probabilityThreshold;
//	}

	/**
	 * Sets a maximum distance threshold for recognition. If the distance to the
	 * average vector of the closest gesture class is higher than distance
	 * threshold, the gesture is non recognized (i.e. method
	 * <code>classify</code> returns null). By default, this distance is set
	 * to 1000.
	 * 
	 * @param dist
	 *            The maximum distance to the average vector of recognized
	 *            class.
	 */
	public void setMaximumDistance(int dist) {
		mahalanobisThreshold = dist;
	}

	/**
	 * @return The maximum distance threshold for recognition
	 */
	public int getMaximumDistance() {
		return mahalanobisThreshold;
	}

	/**
	 * {@inheritDoc}
	 */
	public Vector<Score> sortedClasses(Gesture g) {
		compile();
		Vector<Score> sortedClasses = new Vector<Score>();

		Vector<Double> fv = getFeatures(g);
		Vector<Vector<Double>> wghts = weights;
		ArrayList<Double> cst = cnst;

//		for (int nc = 0; nc < classes.size(); nc++) {
//		double value = VectorUtility.scalarProduct(wghts.get(nc), fv) + cst.get(nc);
//		if (nc == 0) {
//		sortedClasses.add(new Score(classes.get(nc).getName(), value));
//		} else {
//		int i = 0;
//		while (i < sortedClasses.size() && sortedClasses.get(i).getScore() > value)
//		i++;
//		sortedClasses.add(i, new Score(classes.get(nc).getName(), value));
//		}
//		}

		Vector<Double> sortedScalarProduct = new Vector<Double>();
		for (int nc = 0; nc < classes.size(); nc++) {
			double value = VectorUtility.scalarProduct(wghts.get(nc), fv) + cst.get(nc);
			if (nc == 0) {
				sortedClasses.add(new Score(classes.get(nc).getName(), 
						mahalanobisDistance(fv, classes.get(nc).getAverage(), invAvgCov)));
				sortedScalarProduct.add(value);
			} else {
				int i = 0;
				while (i < sortedScalarProduct.size() && sortedScalarProduct.get(i) > value)
					i++;
				sortedClasses.add(i, new Score(classes.get(nc).getName(), 
						mahalanobisDistance(fv, classes.get(nc).getAverage(), invAvgCov)));
				sortedScalarProduct.add(i, value);
			}
		}
		return sortedClasses;
	}

	/**
	 * Computes the vector of features for a gesture.
	 * @param g The gesture
	 * @return The vector of features.
	 */
	public static Vector<Double> getFeatures(Gesture g) {

		Vector<Double> compiledData = new Vector<Double>();
		for (int i = 0; i < NAME_FEATURES.length; i++)
			compiledData.add(0.0);

		if (g.getPoints().size() < 3)
			return compiledData; // a feature vector of all zeros, at least 3
		// points are required to compute initial
		// sin and cos

		// initial cos and sin
		/* compute initial theta */
		Point2D thirdPoint = g.getPoints().get(2);
		/* find angle w.r.t. positive x axis e.g. (1,0) */
		double dx = thirdPoint.getX() - g.getStart().getX();
		double dy = thirdPoint.getY() - g.getStart().getY();
		double dist2 = dx * dx + dy * dy;
		if (dist2 > DIST_SQ_THRESHOLD) {
			double d = Math.sqrt(dist2);
			compiledData.set(PF_INIT_COS, dx / d);
			compiledData.set(PF_INIT_SIN, dy / d);
		}

		// compute features related to bounding box (length and orientation)
		double t1 = g.getMax().getX() - g.getMin().getX();
		double t2 = g.getMax().getY() - g.getMin().getY();
		double bblen = Math.sqrt(t1 * t1 + t2 * t2);
		compiledData.set(PF_BB_LEN, bblen);
		if (bblen * bblen > DIST_SQ_THRESHOLD) {
			double tmp = Math.atan2(t2, t1);
			compiledData.set(PF_BB_TH, tmp);
		}

		t1 = g.getEnd().getX() - g.getStart().getX();
		t2 = g.getEnd().getY() - g.getStart().getY();
		double selen = Math.sqrt(t1 * t1 + t2 * t2);
		compiledData.set(PF_SE_LEN, selen);
		double factor = selen * selen / SE_TH_ROLLOFF;
		if (factor > 1.0)
			factor = 1.0;
		factor = selen > EPSILON ? factor / selen : 0.0;

		compiledData.set(PF_SE_COS, (g.getEnd().getX() - g.getStart().getX()) * factor);
		compiledData.set(PF_SE_SIN, (g.getEnd().getY() - g.getStart().getY()) * factor);

		Point2D next = null;
		Point2D previous = null;
		double magsq = 0;
		int i = 1;
		double length = 0;
		double rotation = 0;
		double sumAbsAngles = 0;
		double sharpness = 0;
		Point2D del = null;
		Point2D delta = new Point2D.Double();
		double maxSpeed = Double.MIN_VALUE;

		for (Iterator<Point2D> iterator = g.getPoints().iterator(); iterator.hasNext();) {
			next = iterator.next();
			if (previous != null) {
				del = new Point2D.Double(previous.getX() - next.getX(), previous.getY() - next.getY()); // delta
				magsq = del.getX() * del.getX() + del.getY() * del.getY();
				if (magsq <= DIST_SQ_THRESHOLD) {
					continue; /* ignore this point */
				}
			}

			double dist = Math.sqrt(magsq);
			length += dist;

			if (i >= 3) {
				double theta1 = del.getX() * delta.getY() - delta.getX() * del.getY();
				double theta2 = del.getX() * delta.getX() + del.getY() * delta.getY();
				double th = Math.atan2(theta1, theta2);
				double absth = Math.abs(th);
				rotation += th;
				sumAbsAngles += absth;
				sharpness += th * th;

				int indexPrevious = g.getPoints().indexOf(previous);
				double lasttime = g.getPointTimes().get(indexPrevious);
				double v = dist / (g.getPointTimes().get(g.getPointTimes().size() - 1) - lasttime);
				if (g.getPointTimes().get(g.getPointTimes().size() - 1) > lasttime && v > maxSpeed)
					maxSpeed = v;
			}

			if (previous != null) {
				delta.setLocation(del.getX(), del.getY());
			}
			previous = next;
			i++;
		}

		compiledData.set(PF_LEN, length);
		compiledData.set(PF_TH, rotation);
		compiledData.set(PF_ATH, sumAbsAngles);
		compiledData.set(PF_SQTH, sharpness);

		compiledData.set(PF_DUR, g.getDuration() * .01); // sensitive to
		// a 1/10th of
		// second

		compiledData.set(PF_MAXV, maxSpeed * 10000);
		return compiledData;
	}

	/**
	 * Computes the vector of features for a list of points. Since we do not have
	 * time information, features related to time are set to NaN 
	 * (<i>path duration</i> and <i>maximum speed</i>).
	 * @param points The list of points
	 * @return The vector of features.
	 */
	public static Vector<Double> getFeatures(Vector<Point2D> points) {

		Vector<Double> compiledData = new Vector<Double>();
		for (int i = 0; i < NAME_FEATURES.length; i++)
			compiledData.add(0.0);

		if (points.size() < 3)
			return compiledData; 
		// a feature vector of all zeros, at least 3
		// points are required to compute initial
		// sin and cos


		Point2D startPoint = points.get(0);
		Point2D thirdPoint = points.get(2);
		Point2D endPoint   = points.get(points.size() - 1);

		// initial cos and sin
		/* compute initial theta */
		/* find angle w.r.t. positive x axis e.g. (1,0) */
		double dx = thirdPoint.getX() - startPoint.getX();
		double dy = thirdPoint.getY() - startPoint.getY();
		double dist2 = dx * dx + dy * dy;
		if (dist2 > DIST_SQ_THRESHOLD) {
			double d = Math.sqrt(dist2);
			compiledData.set(PF_INIT_COS, dx / d);
			compiledData.set(PF_INIT_SIN, dy / d);
		}

		// compute features related to bounding box (length and orientation)
		Rectangle2D bb = GestureUtils.boundingBox(points);
		double t1 = bb.getMaxX() - bb.getMinX();
		double t2 = bb.getMaxY() - bb.getMinY();
		double bblen = Math.sqrt(t1 * t1 + t2 * t2);
		compiledData.set(PF_BB_LEN, bblen);
		if (bblen * bblen > DIST_SQ_THRESHOLD) {
			double tmp = Math.atan2(t2, t1);
			compiledData.set(PF_BB_TH, tmp);
		}

		t1 = endPoint.getX() - startPoint.getX();
		t2 = endPoint.getY() - startPoint.getY();
		double selen = Math.sqrt(t1 * t1 + t2 * t2);
		compiledData.set(PF_SE_LEN, selen);
		double factor = selen * selen / SE_TH_ROLLOFF;
		if (factor > 1.0)
			factor = 1.0;
		factor = selen > EPSILON ? factor / selen : 0.0;

		compiledData.set(PF_SE_COS, (endPoint.getX() - startPoint.getX()) * factor);
		compiledData.set(PF_SE_SIN, (endPoint.getY() - startPoint.getY()) * factor);

		Point2D next = null;
		Point2D previous = null;
		double magsq = 0;
		int i = 1;
		double length = 0;
		double rotation = 0;
		double sumAbsAngles = 0;
		double sharpness = 0;
		Point2D del = null;
		Point2D delta = new Point2D.Double();

		for (Iterator<Point2D> iterator = points.iterator(); iterator.hasNext();) {
			next = iterator.next();
			if (previous != null) {
				del = new Point2D.Double(previous.getX() - next.getX(), previous.getY() - next.getY()); // delta
				magsq = del.getX() * del.getX() + del.getY() * del.getY();
				if (magsq <= DIST_SQ_THRESHOLD) {
					continue; /* ignore this point */
				}
			}

			double dist = Math.sqrt(magsq);
			length += dist;

			if (i >= 3) {
				double theta1 = del.getX() * delta.getY() - delta.getX() * del.getY();
				double theta2 = del.getX() * delta.getX() + del.getY() * delta.getY();
				double th = Math.atan2(theta1, theta2);
				double absth = Math.abs(th);
				rotation += th;
				sumAbsAngles += absth;
				sharpness += th * th;

			}

			if (previous != null) {
				delta.setLocation(del.getX(), del.getY());
			}
			previous = next;
			i++;
		}

		compiledData.set(PF_LEN, length);
		compiledData.set(PF_TH, rotation);
		compiledData.set(PF_ATH, sumAbsAngles);
		compiledData.set(PF_SQTH, sharpness);

		// cannot compute features related to time
		compiledData.set(PF_DUR, Double.NaN);
		compiledData.set(PF_MAXV, Double.NaN);

		return compiledData;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeExample(Gesture gesture) {
		for (Iterator<RubineGestureClass> iterator = classes.iterator(); iterator.hasNext();) {
			RubineGestureClass next = iterator.next();
			if(next != null) next.removeExample(gesture);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void addExample(String className, Gesture example) {
		int index = classesNames.indexOf(className);
		if(index == -1) return;
		RubineGestureClass gestureClass = classes.get(index);
		if(gestureClass != null) gestureClass.addExample(example);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void renameClass(String previousClassName, String newClassName) {
		int index = classesNames.indexOf(previousClassName);
		if(index == -1) return;
		RubineGestureClass gc = classes.get(index);
		gc.setName(newClassName);
		super.renameClass(previousClassName, newClassName);
	}

	/**
	 * {@inheritDoc}
	 */
	public Vector<Gesture> getExamples(String className)
			throws UnsupportedOperationException {
		int index = classesNames.indexOf(className);
		if(index == -1) return null;
		RubineGestureClass gc = classes.get(index);
		return gc.getGestures();
	}
	
}
