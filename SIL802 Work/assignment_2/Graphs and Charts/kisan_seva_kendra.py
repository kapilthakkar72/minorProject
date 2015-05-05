import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [2,10]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Do you know about Kisan seva Kendra?')
plt.ylabel('Number of Farmers')
plt.title('Knowledge about Kisan seva Kendra')

plt.ylim([0,11])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('Yes','No') )
    
plt.show()