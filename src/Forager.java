import java.util.LinkedList;
import java.util.List;

class Forager extends Ant {

    LinkedList<EnvironmentNode> moveHistory;
    boolean hasFood;
    int moveHistoryIndex;

    Forager(World world) {
        super(AntType.FORAGER, world);
        hasFood = false;
        moveHistory = new LinkedList<>();
    }

    @Override
    protected void activate() {

        if (!hasFood) {
            // Forage Mode
            if (currentNode.hasFood()) {
                // Take food if available
                currentNode.takeFood();
                hasFood = true;
            } else {
                moveHistory.add(currentNode);
                // Deal with move history and look for loops
                // If we've seen this node before we are looping and every node between
                // the first occurrence and this one can be ignored
                // Examine every move up to the current node
                EnvironmentNode lastPathTaken;
                List<EnvironmentNode> priorPath = moveHistory.subList(0, moveHistory.size()-1);
                if (priorPath.contains(currentNode)) {
                    // Find the last time we've visited this node
                    int lastVisitedIndex = priorPath.indexOf(currentNode);
                    // and save the direction we went before
                    lastPathTaken = priorPath.get(lastVisitedIndex+1);

                    // Now clear the move history from that point to remove redundancies
                    moveHistory.subList(lastVisitedIndex+1, moveHistory.size()).clear();

                    // Now establish new valid move alternatives and move randomly
                    // amongst those in an attempt to break the cycle
                    List<EnvironmentNode> moveOptions = currentNode.getExploredAdjacentNodes();

                    // If we can, remove the square we just came from
                    if (moveOptions.size() > 2 && moveHistory.size() >= 2) {
                        moveOptions.remove(moveHistory.get(moveHistory.size()-2));
                    }

                    // If we can, remove the last path we took (that entered the loop)
                    if (moveOptions.size() > 1) {
                        moveOptions.remove(lastPathTaken);
                    }

                    // Now move randomly among the options that remain
                    randomMove(moveOptions);
                } else {
                    // If we are not looping, follow the pheromone trails

                    // Get all available move options
                    List<EnvironmentNode> adjacentExplored = currentNode.getExploredAdjacentNodes();

                    // If we can, remove the square we just visited
                    if (adjacentExplored.size() > 1 && moveHistory.size() >= 2) {
                       adjacentExplored.remove(moveHistory.get(moveHistory.size()-2));
                    }

                    // Now find the square or squares with the max pheromone level
                    int maxPheromone = 0;
                    List<EnvironmentNode> foodTrails = new LinkedList<>();
                    for (EnvironmentNode n : adjacentExplored) {
                        int pLevel = n.getPheromoneAmount();
                        if (pLevel == maxPheromone) {
                            foodTrails.add(n);
                        } else if (pLevel > maxPheromone) {
                            foodTrails.clear();
                            foodTrails.add(n);
                            maxPheromone = pLevel;
                        }
                    }

                    // Move randomly to a square with the highest pheromone levels
                    randomMove(foodTrails);
                }
            }
        } else {
            // Return to nest mode
            if (currentNode.isEntrance()){
                currentNode.addFood(1);
                hasFood = false;
                moveHistory.clear();
            } else {
                if (currentNode.getPheromoneAmount() < 1000) {
                    currentNode.addPheromone(10);
                }
                move(moveHistory.getLast());
                moveHistory.removeLast();
            }
        }
    }

}
