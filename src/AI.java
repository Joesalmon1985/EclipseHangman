
import java.util.Arrays;
import java.util.HashMap;

import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.ConnectionFactory;

@SuppressWarnings("unused")
public class AI {
	Layer inputLayer;
	Layer outputLayer;
	Layer hiddenLayerOne;
	Layer hiddenLayerTwo;
	MultiLayerPerceptron ann;
	int inputSize;
	int outputSize;
	DataSet ds;
	HashMap <Double, double[]> guessIdeas = new HashMap <Double, double[]>();

	public AI(int inputs, int hiddenOne, int hiddenTwo, int outputs) {
		makeNN(inputs,hiddenOne,hiddenTwo,outputs);
		makeDS(inputs, outputs);
		System.out.println("made new AI");
	}

	public static void main(String[] args) {
	}
	
	public void makeDS(int inputSize, int outputSize) {
		ds = new DataSet(inputSize, outputSize);
	}
	
	public void makeNN(int inputs, int hiddenOne, int hiddenTwo, int outputs) {
		//makeInputLayer(inputs);
		//makeHiddenLayerOne(hiddenOne);
		//makeHiddenLayerTwo(hiddenTwo);
		//makeOutputLayer(outputs);
		makeNetwork(inputs,hiddenOne,hiddenTwo,outputs);
		//connectLayers();
		makeDS(inputs,outputs);
	}
	
	private void connectLayers() {
		ConnectionFactory.fullConnect(ann.getLayerAt(0), ann.getLayerAt(1));
		ConnectionFactory.fullConnect(ann.getLayerAt(1), ann.getLayerAt(2));
		ConnectionFactory.fullConnect(ann.getLayerAt(2), ann.getLayerAt(3));
		ConnectionFactory.fullConnect(ann.getLayerAt(0),ann.getLayerAt(ann.getLayersCount()-1), false);
		ann.setInputNeurons(inputLayer.getNeurons());
		ann.setOutputNeurons(outputLayer.getNeurons());
	}

	private void makeNetwork(int inputs, int hiddenOne, int hiddenTwo, int outputs) {
		ann= new MultiLayerPerceptron(inputs,hiddenOne,hiddenTwo,outputs);
		// ann.addLayer(0, inputLayer);
		// ann.addLayer(1, hiddenLayerOne);
		// ann.addLayer(2,hiddenLayerTwo);
		// ann.addLayer(3,outputLayer);
	}

	private void makeOutputLayer(int numberOutputs) {
		outputLayer = new Layer();
		for (int i = 0 ; i < numberOutputs ; i ++)
		{
			outputLayer.addNeuron(new Neuron());
		}
}

	// Creates a hidden Layer with a given number of inputs
	private void makeHiddenLayerOne(int numberInputs) {
		hiddenLayerOne = new Layer();
		for (int i = 0 ; i < numberInputs ; i ++)
		{
			hiddenLayerOne.addNeuron(new Neuron());
		}
	}
		
	// Creates a hidden Layer with a given number of inputs
	private void makeHiddenLayerTwo(int numberInputs) {
			hiddenLayerTwo = new Layer();
			for (int i = 0 ; i < numberInputs ; i ++)
			{
				hiddenLayerTwo.addNeuron(new Neuron());
			}
	}

	// Creates an input Layer with a given number of inputs.
	private void makeInputLayer(int numberInputs) {
		inputLayer = new Layer();
		for (int i = 0 ; i < numberInputs ; i ++)
		{
			inputLayer.addNeuron(new Neuron());
		}
	}

	private void createDS(StringBuilder gameState) {
		int toAdd = 0;
		double[] newInput = new double[30];
		double[] newOutput = new double[1];
		for (int i = 0 ; i < 29 ; i ++) {
			toAdd = gameState.indexOf(",", toAdd);
			newInput[i] = gameState.charAt(i);
		}
	}
	
	private void train() {
		BackPropagation backPropagation = new BackPropagation();
		backPropagation.setMaxIterations(100);
		ann.save("network");
		ds.save("data");
		ann.learn(ds, backPropagation);
		System.out.print("trained");
		}

	public char getGuess(double[] newInputs, double[] newOutput) {
		ds.addRow(newInputs, newOutput);
		double[] bestResult = new double[] {0};
		Double bestIdea = 25.0;
		double[] testResult = new double[] {0};
		train();
		generateIdeas(newInputs);
		for (double i = 25 ; i > 25 - guessIdeas.size(); i --) {
			testResult = testIdeas(makeTest(i));
			if (testResult[0]>bestResult[0]) {
				System.out.print("found a better idea!");
				bestResult[0] = testResult[0];
				bestIdea = i;
			}
		}
		double[] toReturn = guessIdeas.get(bestIdea);
		return (char)toReturn[(int) (newInputs[30]-1)];
	}
	
	private double[] makeTest(Double tryNumb) {
		return guessIdeas.get(tryNumb);
	}
		
	private void generateIdeas (double[] newInputs) {
		guessIdeas.clear();
		for (double i = newInputs[30]-1 ; i < 26 ; i ++) {
			double[] newGuess = new double[31];
			for (int k = 0 ; k < newInputs[30]-1 ; k ++) {
				newGuess[k]=newInputs[k];
			}
			newGuess[(int)newInputs[30]-1] = newInputs[(char) i];
			guessIdeas.put(i, newGuess);
		}
	}

	private double[] testIdeas (double[] testInputs) {
		ann.setInput(testInputs);
		ann.calculate();
		System.out.print("testing" + (char)ann.getOutput()[0]);
		return ann.getOutput();
		
	}
}