import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [6,3,2,1]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('How freqently farmers visit Mandi?')
plt.ylabel('Number of Farmers')
plt.title('Freqency of mandi visit')

plt.ylim([0,11])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('Daily','When crop is ready','4-5 days','Per month') )
    
plt.show()