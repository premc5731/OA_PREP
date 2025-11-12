import React from 'react';

function Score({ score, total, onRestart }) {
  const percentage = Math.round((score / total) * 100);

  return (
    <div className="quiz-card score-card">
      <h2>Quiz Complete!</h2>
      <p>Your Score:</p>
      <div className="score-display">
        {score} / {total}
      </div>
      <div className="score-percentage">
        {percentage}%
      </div>
      <button className="action-button" onClick={onRestart}>
        Restart Quiz
      </button>
    </div>
  );
}

export default Score;