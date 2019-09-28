# Cinemacraft

<br /><br />
![Cinemacraft: Operacraft](http://siddharthnarayanan.com/wp-content/uploads/2019/08/operacraft.jpg)
<br /><br />

Cinemacraft is a novel technology-mediated immersive machinima platform for collaborative performance and musical human-computer interaction. It innovates on a custom, reverse-engineered version of Minetest to offer a collection of live theatrical and cinematic production tools, and leverages the Microsoft Kinect HD for embodied interaction, including posture, arm movement, facial expressions, and through the sensory fusion lip syncing based on captured voice input. It is designed as an out-of-box turnkey solution that sidesteps the uncanny valley by utilizing the extremely popular sandbox-like gaming environment, for simple yet compelling storytelling along with multiple live camera views, scene changes, subtitles, lip sync, production-centric stage cues, and virtual audience. The platform is aimed at extending the frontiers of collaborative content creation as well as broadening audience impact to enhance creativity and emotional experiences.


## Publications

[Cinemacraft: exploring fidelity cues in collaborative virtual world interactions
S Narayanan, N Polys, II Bukvic - Virtual Reality, 2019](https://link.springer.com/article/10.1007/s10055-019-00382-0)

[*S. Narayanan, and I. Bukvic, “Cinemacraft: Immersive Live Machinima as an Empathetic Musical Storytelling Platform,” International Computer Music Conference, Shanghai, China, 2017, pp. 384-389](https://quod.lib.umich.edu/i/icmc/bbp2372.2017.065/--cinemacraft-immersive-live-machinima-as-an-empathetic?view=image)

This is a simple example implementation of bert-as-a-service for sentence similarity.

1. Install the required server and client

```python
pip install bert-serving-server  # server
pip install bert-serving-client  # client, independent of `bert-serving-server`
```

2. Start the BERT service. Note that you will have to choose the correct path and pre-trained model name for BERT.

```python
bert-serving-start -model_dir /tmp/english_L-12_H-768_A-12/ -num_worker=4 
```

3. Run your test script

```python
python BERT_test.py
```

