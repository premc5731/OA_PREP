import React, { useState } from 'react';

function Quiz({ questions, onQuizEnd }) {
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState(null); // The index of the selected option
  const [score, setScore] = useState(0);
  const [showFeedback, setShowFeedback] = useState(false);
  const [isCorrect, setIsCorrect] = useState(false);

  const currentQuestion = questions[currentQuestionIndex];

  const handleOptionClick = (optionIndex) => {
    if (showFeedback) return; 

    setSelectedAnswer(optionIndex);
    const correct = optionIndex === currentQuestion.correctAnswerIndex;
    setIsCorrect(correct);
    setShowFeedback(true);

    setTimeout(() => {
      handleNextClick();
    }, 1500);
  };

  const handleNextClick = () => {
    if (selectedAnswer === currentQuestion.correctAnswerIndex) {
      setScore(score + 1);
    }

    setSelectedAnswer(null);
    setShowFeedback(false);
    setIsCorrect(false);

    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      const finalScore = (selectedAnswer === currentQuestion.correctAnswerIndex) ? score + 1 : score;
      onQuizEnd(finalScore);
    }
  };

  return (
    <div className="quiz-card">
      <div className="progress-bar">
        <div
          className="progress-fill"
          style={{ width: `${((currentQuestionIndex + 1) / questions.length) * 100}%` }}
        ></div>
      </div>
      <div className="question-header">
        <h2>Question {currentQuestionIndex + 1} of {questions.length}</h2>
        <p>{currentQuestion.questionText}</p>
      </div>
      
      <div className="options-container">
        {currentQuestion.options.map((option, index) => {
          let buttonClass = 'option-button';
          if (selectedAnswer === index) {
            buttonClass += showFeedback
              ? (isCorrect ? ' correct' : ' incorrect')
              : ' selected';
          }
          if (showFeedback && index === currentQuestion.correctAnswerIndex && selectedAnswer !== index) {
            buttonClass += ' correct-answer';
          }

          return (
            <button
              key={index}
              className={buttonClass}
              onClick={() => handleOptionClick(index)}
              disabled={showFeedback}
            >
              {option}
              {showFeedback && selectedAnswer === index && (
                <span className="feedback-icon">
                  {isCorrect ? '✓' : '✗'}
                </span>
              )}
              {showFeedback && index === currentQuestion.correctAnswerIndex && selectedAnswer !== index && (
                <span className="feedback-icon correct-indicator">✓</span>
              )}
            </button>
          );
        })}
      </div>

      {showFeedback && (
        <div className={`feedback-message ${isCorrect ? 'correct-feedback' : 'incorrect-feedback'}`}>
          {isCorrect ? 'Correct! Well done!' : 'Incorrect. The correct answer is highlighted.'}
        </div>
      )}

      <button
        className="action-button"
        onClick={handleNextClick}
        disabled={selectedAnswer === null || showFeedback} // Disable during feedback
      >
        {currentQuestionIndex < questions.length - 1 ? 'Next' : 'Finish'}
      </button>
    </div>
  );
}

export default Quiz;