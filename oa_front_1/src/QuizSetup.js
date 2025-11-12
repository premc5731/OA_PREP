import React, { useState } from 'react';

function QuizSetup({ onStartQuiz }) {
  const [topic, setTopic] = useState('Java');
  const [count, setCount] = useState(5);

  const handleSubmit = (e) => {
    e.preventDefault();
    onStartQuiz(topic, count);
  };

  return (
    <form className="quiz-card" onSubmit={handleSubmit}>
      <h2>Quiz Setup</h2>
      <div className="form-group">
        <label htmlFor="topic">Topic:</label>
        <input
          id="topic"
          type="text"
          value={topic}
          onChange={(e) => setTopic(e.target.value)}
          required
        />
      </div>
      <div className="form-group">
        <label htmlFor="count">Number of Questions:</label>
        <input
          id="count"
          type="number"
          value={count}
          onChange={(e) => setCount(parseInt(e.target.value, 10))}
          min="1"
          max="20"
          required
        />
      </div>
      <button type="submit" className="action-button">Start Quiz</button>
    </form>
  );
}

export default QuizSetup;